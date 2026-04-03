package io.proleap.cobol.engine.verification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class VerificationFeedbackBundle {

    public enum FailureCategory {
        COMPILE_ERROR,
        POLICY_VIOLATION,
        STATIC_ANALYSIS_FAILURE,
        TEST_FAILURE,
        GOLDEN_MASTER_MISMATCH,
        RUNTIME_GAP,
        FIXTURE_PROBLEM,
        ENVIRONMENT_PROBLEM
    }

    private final String bundleId;
    private final String targetId;
    private final String targetKind;
    private final String priorAgentRunId;
    private final FailureCategory failureCategory;
    private final String failureSummary;
    private final String remediationObjective;
    private final int retryCount;
    private final List<String> failingChecks;
    private final String recommendedEscalationTarget;

    public VerificationFeedbackBundle(final String bundleId, final String targetId, final String targetKind,
            final String priorAgentRunId, final FailureCategory failureCategory, final String failureSummary,
            final String remediationObjective, final int retryCount, final List<String> failingChecks,
            final String recommendedEscalationTarget) {
        this.bundleId = Objects.requireNonNull(bundleId, "bundleId");
        this.targetId = Objects.requireNonNull(targetId, "targetId");
        this.targetKind = Objects.requireNonNull(targetKind, "targetKind");
        this.priorAgentRunId = Objects.requireNonNull(priorAgentRunId, "priorAgentRunId");
        this.failureCategory = Objects.requireNonNull(failureCategory, "failureCategory");
        this.failureSummary = Objects.requireNonNull(failureSummary, "failureSummary");
        this.remediationObjective = Objects.requireNonNull(remediationObjective, "remediationObjective");
        this.retryCount = retryCount;
        this.failingChecks = failingChecks == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(failingChecks));
        this.recommendedEscalationTarget = recommendedEscalationTarget == null ? "codegen" : recommendedEscalationTarget;
    }

    public String getBundleId() {
        return bundleId;
    }

    public List<String> getFailingChecks() {
        return failingChecks;
    }

    public FailureCategory getFailureCategory() {
        return failureCategory;
    }

    public String getFailureSummary() {
        return failureSummary;
    }

    public String getPriorAgentRunId() {
        return priorAgentRunId;
    }

    public String getRecommendedEscalationTarget() {
        return recommendedEscalationTarget;
    }

    public String getRemediationObjective() {
        return remediationObjective;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getTargetKind() {
        return targetKind;
    }
}
