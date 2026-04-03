package io.proleap.cobol.engine.orchestration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import io.proleap.cobol.engine.agent.AgentTaskContract;
import io.proleap.cobol.engine.agent.HandoffPacket;
import io.proleap.cobol.engine.codegen.GeneratedProject;
import io.proleap.cobol.engine.codegen.JavaSpringCodeGenerator;
import io.proleap.cobol.engine.ingest.IngestionManifest;
import io.proleap.cobol.engine.ingest.ProgramWorkspace;
import io.proleap.cobol.engine.ingest.SourceUnit;
import io.proleap.cobol.engine.ir.IrProgram;
import io.proleap.cobol.engine.ir.IrProgramBuilder;
import io.proleap.cobol.engine.ir.IrStatement;
import io.proleap.cobol.engine.parse.ParseArtifact;
import io.proleap.cobol.engine.parse.ParseArtifactService;
import io.proleap.cobol.engine.slice.ConversionSlice;
import io.proleap.cobol.engine.slice.IrSlicePackager;
import io.proleap.cobol.engine.slice.SliceBundle;
import io.proleap.cobol.engine.verification.VerificationFeedbackBundle;
import io.proleap.cobol.engine.verification.VerificationHarness;
import io.proleap.cobol.engine.verification.VerificationReport;

public class ConversionEngine {

    private final ParseArtifactService parseArtifactService = new ParseArtifactService();
    private final IrProgramBuilder irProgramBuilder = new IrProgramBuilder();
    private final IrSlicePackager slicePackager = new IrSlicePackager();
    private final JavaSpringCodeGenerator codeGenerator = new JavaSpringCodeGenerator();
    private final VerificationHarness verificationHarness = new VerificationHarness();

    public List<ProgramConversionRecord> convert(final IngestionManifest manifest) throws IOException {
        final List<ProgramConversionRecord> records = new ArrayList<ProgramConversionRecord>();
        for (final ProgramWorkspace workspace : manifest.getProgramWorkspaces()) {
            records.add(convertWorkspace(workspace));
        }
        return records;
    }

    private ProgramConversionRecord convertWorkspace(final ProgramWorkspace workspace) throws IOException {
        final List<ParseArtifact> parseArtifacts = new ArrayList<ParseArtifact>();
        final List<IrStatement> statements = new ArrayList<IrStatement>();
        final List<String> diagnostics = new ArrayList<String>();
        final List<String> sourceFiles = new ArrayList<String>();
        final Set<String> copybookDirectories = new LinkedHashSet<String>();
        ParseArtifact primaryArtifact = null;

        for (final SourceUnit sourceUnit : workspace.getSourceUnits()) {
            final ParseArtifact parseArtifact = parseArtifactService.parse(sourceUnit);
            if (primaryArtifact == null) {
                primaryArtifact = parseArtifact;
            }
            parseArtifacts.add(parseArtifact);
            sourceFiles.add(sourceUnit.getSourceFile().getPath());
            for (final java.io.File dir : sourceUnit.getCopybookDirectories()) {
                copybookDirectories.add(dir.getPath());
            }
            final IrProgram unitProgram = irProgramBuilder.build(parseArtifact);
            statements.addAll(unitProgram.getStatements());
            diagnostics.addAll(unitProgram.getDiagnostics());
        }

        if (primaryArtifact == null) {
            throw new IllegalArgumentException("Program workspace must contain at least one source unit");
        }

        final String programName = sanitizeProgramName(workspace.getWorkspaceId());
        final IrProgram irProgram = new IrProgram("v1", programName, statements, diagnostics);
        final SliceBundle sliceBundle = slicePackager.packageProgram(irProgram);
        final GeneratedProject generatedProject = codeGenerator.generate(irProgram);
        final VerificationReport verificationReport = verificationHarness.verify(irProgram, generatedProject);
        final DependencySummary dependencySummary = new DependencySummary(workspace.getWorkspaceId(),
                workspace.getSourceUnits().size(), sourceFiles, new ArrayList<String>(copybookDirectories));

        final List<AgentTaskContract> agentTaskContracts = new ArrayList<AgentTaskContract>();
        final List<HandoffPacket> handoffPackets = new ArrayList<HandoffPacket>();
        for (final ConversionSlice slice : sliceBundle.getSlices()) {
            final AgentTaskContract contract = new AgentTaskContract(
                    slice.getSliceId(),
                    "human-agent-bounded-implementation",
                    slice.getSliceId(),
                    irProgram.getProgramName(),
                    "domain_service_component",
                    "Implement or remediate only the bounded generated slice.",
                    Arrays.asList(
                            "src/main/java/io/proleap/generated/" + irProgram.getProgramName().toLowerCase() + "/"
                                    + irProgram.getProgramName() + "Program.java",
                            "src/main/java/io/proleap/generated/" + irProgram.getProgramName().toLowerCase() + "/GeneratedApplication.java"),
                    slice.getAcceptanceCriteria(),
                    Arrays.asList(
                            "Stop if runtime API changes are required.",
                            "Stop if edits exceed allowed generated files."),
                    2);
            agentTaskContracts.add(contract);
            handoffPackets.add(new HandoffPacket(
                    slice.getSliceId() + "-handoff",
                    contract.getTaskId(),
                    slice.getSliceId(),
                    irProgram.getProgramName(),
                    "Human developer chooses any coding agent and applies changes only within allowed files and zones.",
                    Arrays.asList("ir-slice-bundle.json", "agent-task-contracts.json", "dependency-summary.json"),
                    Arrays.asList(
                            "Record prompt summary and chosen model.",
                            "Stay within allowed files.",
                            "Resubmit for deterministic validation.")));
        }

        final ConversionSlice firstSlice = sliceBundle.getSlices().get(0);
        final List<AgentIterationRecord> iterationRecords = verificationReport.isPassed()
                ? Collections.singletonList(new AgentIterationRecord(firstSlice.getSliceId() + "-iteration-1",
                        firstSlice.getSliceId(), "accepted", null))
                : Collections.singletonList(new AgentIterationRecord(
                        firstSlice.getSliceId() + "-iteration-1",
                        firstSlice.getSliceId(),
                        "retry_pending",
                        new VerificationFeedbackBundle(
                                firstSlice.getSliceId() + "-feedback-1",
                                firstSlice.getSliceId(),
                                "slice",
                                firstSlice.getSliceId() + "-human-agent-run-1",
                                VerificationFeedbackBundle.FailureCategory.GOLDEN_MASTER_MISMATCH,
                                "Generated output did not satisfy deterministic checks.",
                                "Fix generated slice without changing runtime APIs.",
                                1,
                                verificationReport.getMismatches(),
                                "codegen")));

        final List<IntegrationCheckpoint> integrationCheckpoints = sliceBundle.getSlices().size() > 1
                ? Collections.singletonList(new IntegrationCheckpoint(
                        programName.toLowerCase() + "-integration-checkpoint-1",
                        verificationReport.isPassed() ? "ready" : "blocked",
                        collectSliceIds(sliceBundle.getSlices())))
                : Collections.emptyList();

        final String convergenceState = sliceBundle.getSlices().size() > 1 && verificationReport.isPassed()
                ? "program_integration_ready"
                : verificationReport.isPassed() ? "program_ready_for_integration" : "slice_retry_required";
        final ProgramConversionRecord.Status status = verificationReport.isPassed()
                ? ProgramConversionRecord.Status.ACCEPTED
                : ProgramConversionRecord.Status.REJECTED;
        return new ProgramConversionRecord(workspace, primaryArtifact, parseArtifacts, irProgram, sliceBundle,
                generatedProject, verificationReport, dependencySummary, agentTaskContracts, handoffPackets,
                iterationRecords, integrationCheckpoints, convergenceState, status);
    }

    private List<String> collectSliceIds(final List<ConversionSlice> slices) {
        final List<String> sliceIds = new ArrayList<String>();
        for (final ConversionSlice slice : slices) {
            sliceIds.add(slice.getSliceId());
        }
        return sliceIds;
    }

    private String sanitizeProgramName(final String value) {
        final String normalized = value == null ? "Program" : value.replaceAll("[^A-Za-z0-9]", " ");
        final StringBuilder builder = new StringBuilder();
        for (final String part : normalized.trim().split("\s+")) {
            if (!part.isEmpty()) {
                builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
            }
        }
        return builder.length() == 0 ? "Program" : builder.toString();
    }
}
