package io.proleap.cobol.engine.orchestration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.proleap.cobol.engine.agent.AgentTaskContract;
import io.proleap.cobol.engine.agent.HandoffPacket;
import io.proleap.cobol.engine.codegen.GeneratedProject;
import io.proleap.cobol.engine.ingest.ProgramWorkspace;
import io.proleap.cobol.engine.ir.IrProgram;
import io.proleap.cobol.engine.parse.ParseArtifact;
import io.proleap.cobol.engine.slice.SliceBundle;
import io.proleap.cobol.engine.verification.VerificationReport;

public class ProgramConversionRecord {

    public enum Status {
        ACCEPTED,
        REJECTED
    }

    private final ProgramWorkspace programWorkspace;
    private final ParseArtifact parseArtifact;
    private final List<ParseArtifact> parseArtifacts;
    private final IrProgram irProgram;
    private final SliceBundle sliceBundle;
    private final GeneratedProject generatedProject;
    private final VerificationReport verificationReport;
    private final DependencySummary dependencySummary;
    private final List<AgentTaskContract> agentTaskContracts;
    private final List<HandoffPacket> handoffPackets;
    private final List<AgentIterationRecord> iterationRecords;
    private final List<IntegrationCheckpoint> integrationCheckpoints;
    private final String convergenceState;
    private final Status status;

    public ProgramConversionRecord(final ProgramWorkspace programWorkspace, final ParseArtifact parseArtifact,
            final List<ParseArtifact> parseArtifacts, final IrProgram irProgram, final SliceBundle sliceBundle,
            final GeneratedProject generatedProject, final VerificationReport verificationReport,
            final DependencySummary dependencySummary, final List<AgentTaskContract> agentTaskContracts,
            final List<HandoffPacket> handoffPackets, final List<AgentIterationRecord> iterationRecords,
            final List<IntegrationCheckpoint> integrationCheckpoints, final String convergenceState, final Status status) {
        this.programWorkspace = Objects.requireNonNull(programWorkspace, "programWorkspace");
        this.parseArtifact = Objects.requireNonNull(parseArtifact, "parseArtifact");
        this.parseArtifacts = parseArtifacts == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<ParseArtifact>(parseArtifacts));
        this.irProgram = Objects.requireNonNull(irProgram, "irProgram");
        this.sliceBundle = Objects.requireNonNull(sliceBundle, "sliceBundle");
        this.generatedProject = Objects.requireNonNull(generatedProject, "generatedProject");
        this.verificationReport = Objects.requireNonNull(verificationReport, "verificationReport");
        this.dependencySummary = Objects.requireNonNull(dependencySummary, "dependencySummary");
        this.agentTaskContracts = agentTaskContracts == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<AgentTaskContract>(agentTaskContracts));
        this.handoffPackets = handoffPackets == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<HandoffPacket>(handoffPackets));
        this.iterationRecords = iterationRecords == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<AgentIterationRecord>(iterationRecords));
        this.integrationCheckpoints = integrationCheckpoints == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<IntegrationCheckpoint>(integrationCheckpoints));
        this.convergenceState = convergenceState == null ? "slice_validation_complete" : convergenceState;
        this.status = Objects.requireNonNull(status, "status");
    }

    public List<AgentTaskContract> getAgentTaskContracts() {
        return agentTaskContracts;
    }

    public String getConvergenceState() {
        return convergenceState;
    }

    public DependencySummary getDependencySummary() {
        return dependencySummary;
    }

    public GeneratedProject getGeneratedProject() {
        return generatedProject;
    }

    public List<HandoffPacket> getHandoffPackets() {
        return handoffPackets;
    }

    public List<IntegrationCheckpoint> getIntegrationCheckpoints() {
        return integrationCheckpoints;
    }

    public IrProgram getIrProgram() {
        return irProgram;
    }

    public List<AgentIterationRecord> getIterationRecords() {
        return iterationRecords;
    }

    public ParseArtifact getParseArtifact() {
        return parseArtifact;
    }

    public List<ParseArtifact> getParseArtifacts() {
        return parseArtifacts;
    }

    public ProgramWorkspace getProgramWorkspace() {
        return programWorkspace;
    }

    public SliceBundle getSliceBundle() {
        return sliceBundle;
    }

    public Status getStatus() {
        return status;
    }

    public VerificationReport getVerificationReport() {
        return verificationReport;
    }
}
