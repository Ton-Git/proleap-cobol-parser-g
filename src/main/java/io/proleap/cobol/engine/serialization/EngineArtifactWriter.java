package io.proleap.cobol.engine.serialization;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.proleap.cobol.engine.agent.AgentTaskContract;
import io.proleap.cobol.engine.agent.HandoffPacket;
import io.proleap.cobol.engine.codegen.GeneratedFile;
import io.proleap.cobol.engine.orchestration.AgentIterationRecord;
import io.proleap.cobol.engine.orchestration.IntegrationCheckpoint;
import io.proleap.cobol.engine.orchestration.ProgramConversionRecord;
import io.proleap.cobol.engine.slice.ConversionSlice;
import io.proleap.cobol.engine.slice.SliceBundle;
import io.proleap.cobol.engine.verification.VerificationFeedbackBundle;

public class EngineArtifactWriter {

    private final EngineJsonSerializer serializer = new EngineJsonSerializer();

    public void write(final File outputDir, final ProgramConversionRecord record) throws IOException {
        outputDir.mkdirs();
        writeJson(new File(outputDir, "program-conversion-record.json"), toProgramConversionRecord(record));
        writeJson(new File(outputDir, "ir-slice-bundle.json"), toSliceBundle(record.getSliceBundle()));
        writeJson(new File(outputDir, "agent-task-contracts.json"), toTaskContracts(record.getAgentTaskContracts()));
        writeJson(new File(outputDir, "human-handoff-packets.json"), toHandoffPackets(record.getHandoffPackets()));
        writeJson(new File(outputDir, "dependency-summary.json"), toDependencySummary(record));
        writeJson(new File(outputDir, "integration-checkpoints.json"), toIntegrationCheckpoints(record));
        writeJson(new File(outputDir, "verification-feedback-bundles.json"), toFeedbackBundles(record.getIterationRecords()));
        writeGeneratedProject(outputDir, record);
        writeHandoffPacketDirectories(outputDir, record);
    }

    private void writeGeneratedProject(final File outputDir, final ProgramConversionRecord record) throws IOException {
        final File generatedProjectDir = new File(outputDir, "generated-project");
        for (final GeneratedFile file : record.getGeneratedProject().getGeneratedFiles()) {
            final File target = new File(generatedProjectDir, file.getPath());
            target.getParentFile().mkdirs();
            Files.writeString(target.toPath(), file.getContent(), StandardCharsets.UTF_8);
        }
    }

    private void writeHandoffPacketDirectories(final File outputDir, final ProgramConversionRecord record) throws IOException {
        for (final HandoffPacket packet : record.getHandoffPackets()) {
            final File packetDir = new File(new File(outputDir, "handoff-packets"), packet.getSliceId());
            packetDir.mkdirs();
            Files.writeString(new File(packetDir, "instructions.md").toPath(), packet.getInstructions(), StandardCharsets.UTF_8);
            Files.writeString(new File(packetDir, "submission-checklist.md").toPath(),
                    String.join("\n", packet.getSubmissionChecklist()), StandardCharsets.UTF_8);
            final AgentTaskContract contract = findTaskContract(record, packet.getTaskId());
            if (contract != null) {
                Files.writeString(new File(packetDir, "task-contract.json").toPath(),
                        serializer.toJson(toTaskContract(contract)), StandardCharsets.UTF_8);
            }
        }
    }

    private AgentTaskContract findTaskContract(final ProgramConversionRecord record, final String taskId) {
        for (final AgentTaskContract contract : record.getAgentTaskContracts()) {
            if (contract.getTaskId().equals(taskId)) {
                return contract;
            }
        }
        return null;
    }

    private List<Object> toFeedbackBundles(final List<AgentIterationRecord> iterationRecords) {
        final List<Object> items = new ArrayList<Object>();
        for (final AgentIterationRecord iterationRecord : iterationRecords) {
            final VerificationFeedbackBundle bundle = iterationRecord.getFeedbackBundle();
            if (bundle != null) {
                final Map<String, Object> item = new LinkedHashMap<String, Object>();
                item.put("schema_version", "1.0.0");
                item.put("feedback_bundle_id", bundle.getBundleId());
                item.put("target_id", bundle.getTargetId());
                item.put("target_kind", bundle.getTargetKind());
                item.put("prior_agent_run_id", bundle.getPriorAgentRunId());
                item.put("failure_category", bundle.getFailureCategory().name().toLowerCase());
                item.put("failure_summary", bundle.getFailureSummary());
                item.put("remediation_objective", bundle.getRemediationObjective());
                item.put("retry_count", bundle.getRetryCount());
                item.put("recommended_escalation_target", bundle.getRecommendedEscalationTarget());
                item.put("failing_checks", new ArrayList<Object>(bundle.getFailingChecks()));
                items.add(item);
            }
        }
        return items;
    }

    private List<Object> toHandoffPackets(final List<HandoffPacket> handoffPackets) {
        final List<Object> items = new ArrayList<Object>();
        for (final HandoffPacket packet : handoffPackets) {
            final Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("packet_id", packet.getPacketId());
            item.put("task_id", packet.getTaskId());
            item.put("slice_id", packet.getSliceId());
            item.put("program_id", packet.getProgramId());
            item.put("instructions", packet.getInstructions());
            item.put("artifact_refs", new ArrayList<Object>(packet.getArtifactRefs()));
            item.put("submission_checklist", new ArrayList<Object>(packet.getSubmissionChecklist()));
            items.add(item);
        }
        return items;
    }

    private Object toDependencySummary(final ProgramConversionRecord record) {
        final Map<String, Object> item = new LinkedHashMap<String, Object>();
        item.put("workspace_id", record.getDependencySummary().getWorkspaceId());
        item.put("source_unit_count", record.getDependencySummary().getSourceUnitCount());
        item.put("source_files", new ArrayList<Object>(record.getDependencySummary().getSourceFiles()));
        item.put("copybook_directories", new ArrayList<Object>(record.getDependencySummary().getCopybookDirectories()));
        return item;
    }

    private Object toIntegrationCheckpoints(final ProgramConversionRecord record) {
        final List<Object> items = new ArrayList<Object>();
        for (final IntegrationCheckpoint checkpoint : record.getIntegrationCheckpoints()) {
            final Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("checkpoint_id", checkpoint.getCheckpointId());
            item.put("state", checkpoint.getState());
            item.put("slice_ids", new ArrayList<Object>(checkpoint.getSliceIds()));
            items.add(item);
        }
        return items;
    }

    private Map<String, Object> toProgramConversionRecord(final ProgramConversionRecord record) {
        final Map<String, Object> json = new LinkedHashMap<String, Object>();
        json.put("schema_version", "1.0.0");
        json.put("workspace_id", record.getProgramWorkspace().getWorkspaceId());
        json.put("program_id", record.getIrProgram().getProgramName());
        json.put("qualification_batch_id", record.getProgramWorkspace().getCorpus());
        json.put("source_unit_count", record.getProgramWorkspace().getSourceUnits().size());
        final Map<String, Object> targetMicroservice = new LinkedHashMap<String, Object>();
        targetMicroservice.put("service_name", "generated-" + record.getIrProgram().getProgramName().toLowerCase());
        targetMicroservice.put("module", record.getIrProgram().getProgramName().toLowerCase());
        targetMicroservice.put("package", "io.proleap.generated." + record.getIrProgram().getProgramName().toLowerCase());
        json.put("target_microservice", targetMicroservice);
        json.put("target_role", "domain_service_component");
        json.put("controller_version", "0.1.0");
        json.put("template_version", "0.1.0");
        json.put("runtime_version", "0.1.0");
        json.put("ir_bundle_id", record.getSliceBundle().getBundleId());
        final List<Object> sliceIds = new ArrayList<Object>();
        for (final ConversionSlice slice : record.getSliceBundle().getSlices()) {
            sliceIds.add(slice.getSliceId());
        }
        json.put("slice_ids", sliceIds);
        json.put("status", record.getStatus().name().toLowerCase());
        json.put("convergence_state", record.getConvergenceState());
        final List<Object> agentRuns = new ArrayList<Object>();
        for (final AgentIterationRecord iterationRecord : record.getIterationRecords()) {
            final Map<String, Object> run = new LinkedHashMap<String, Object>();
            run.put("iteration_id", iterationRecord.getIterationId());
            run.put("task_id", iterationRecord.getTaskId());
            run.put("state", iterationRecord.getState());
            agentRuns.add(run);
        }
        json.put("agent_runs", agentRuns);
        final Map<String, Object> validationSummary = new LinkedHashMap<String, Object>();
        validationSummary.put("compile_passed", record.getVerificationReport().isPassed());
        validationSummary.put("static_analysis_passed", record.getVerificationReport().isPassed());
        validationSummary.put("tests_passed", record.getVerificationReport().isPassed());
        validationSummary.put("golden_master_passed", record.getVerificationReport().isPassed());
        json.put("validation_summary", validationSummary);
        return json;
    }

    private Map<String, Object> toSliceBundle(final SliceBundle bundle) {
        final Map<String, Object> json = new LinkedHashMap<String, Object>();
        json.put("schema_version", "1.0.0");
        json.put("bundle_id", bundle.getBundleId());
        json.put("program_id", bundle.getProgramName());
        final List<Object> slices = new ArrayList<Object>();
        for (final ConversionSlice slice : bundle.getSlices()) {
            final Map<String, Object> item = new LinkedHashMap<String, Object>();
            item.put("slice_id", slice.getSliceId());
            item.put("slice_type", slice.getSliceType());
            item.put("acceptance_checks", new ArrayList<Object>(slice.getAcceptanceCriteria()));
            slices.add(item);
        }
        json.put("slices", slices);
        return json;
    }

    private List<Object> toTaskContracts(final List<AgentTaskContract> contracts) {
        final List<Object> items = new ArrayList<Object>();
        for (final AgentTaskContract contract : contracts) {
            items.add(toTaskContract(contract));
        }
        return items;
    }

    private Map<String, Object> toTaskContract(final AgentTaskContract contract) {
        final Map<String, Object> item = new LinkedHashMap<String, Object>();
        item.put("schema_version", "1.0.0");
        item.put("task_id", contract.getTaskId());
        item.put("task_type", contract.getTaskType());
        item.put("slice_id", contract.getSliceId());
        item.put("program_id", contract.getProgramId());
        item.put("target_role", contract.getTargetRole());
        item.put("purpose", contract.getPurpose());
        item.put("allowed_target_files", new ArrayList<Object>(contract.getAllowedFiles()));
        item.put("acceptance_checks", new ArrayList<Object>(contract.getAcceptanceChecks()));
        item.put("stop_conditions", new ArrayList<Object>(contract.getStopConditions()));
        item.put("retry_budget", contract.getRetryBudget());
        return item;
    }

    private void writeJson(final File target, final Object data) throws IOException {
        Files.writeString(target.toPath(), serializer.toJson(data), StandardCharsets.UTF_8);
    }
}
