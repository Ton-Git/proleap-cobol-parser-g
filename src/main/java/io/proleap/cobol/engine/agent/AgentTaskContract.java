package io.proleap.cobol.engine.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class AgentTaskContract {

    private final String taskId;
    private final String taskType;
    private final String sliceId;
    private final String programId;
    private final String targetRole;
    private final String purpose;
    private final List<String> allowedFiles;
    private final List<String> acceptanceChecks;
    private final List<String> stopConditions;
    private final int retryBudget;

    public AgentTaskContract(final String taskId, final String taskType, final String sliceId, final String programId,
            final String targetRole, final String purpose, final List<String> allowedFiles,
            final List<String> acceptanceChecks, final List<String> stopConditions, final int retryBudget) {
        this.taskId = Objects.requireNonNull(taskId, "taskId");
        this.taskType = Objects.requireNonNull(taskType, "taskType");
        this.sliceId = Objects.requireNonNull(sliceId, "sliceId");
        this.programId = Objects.requireNonNull(programId, "programId");
        this.targetRole = Objects.requireNonNull(targetRole, "targetRole");
        this.purpose = Objects.requireNonNull(purpose, "purpose");
        this.allowedFiles = allowedFiles == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<String>(allowedFiles));
        this.acceptanceChecks = acceptanceChecks == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(acceptanceChecks));
        this.stopConditions = stopConditions == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(stopConditions));
        this.retryBudget = retryBudget;
    }

    public List<String> getAcceptanceChecks() {
        return acceptanceChecks;
    }

    public List<String> getAllowedFiles() {
        return allowedFiles;
    }

    public String getProgramId() {
        return programId;
    }

    public String getPurpose() {
        return purpose;
    }

    public int getRetryBudget() {
        return retryBudget;
    }

    public String getSliceId() {
        return sliceId;
    }

    public List<String> getStopConditions() {
        return stopConditions;
    }

    public String getTargetRole() {
        return targetRole;
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskType() {
        return taskType;
    }
}
