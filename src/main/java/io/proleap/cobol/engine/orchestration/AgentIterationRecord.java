package io.proleap.cobol.engine.orchestration;

import java.util.Objects;

import io.proleap.cobol.engine.verification.VerificationFeedbackBundle;

public class AgentIterationRecord {

    private final String iterationId;
    private final String taskId;
    private final String state;
    private final VerificationFeedbackBundle feedbackBundle;

    public AgentIterationRecord(final String iterationId, final String taskId, final String state,
            final VerificationFeedbackBundle feedbackBundle) {
        this.iterationId = Objects.requireNonNull(iterationId, "iterationId");
        this.taskId = Objects.requireNonNull(taskId, "taskId");
        this.state = Objects.requireNonNull(state, "state");
        this.feedbackBundle = feedbackBundle;
    }

    public VerificationFeedbackBundle getFeedbackBundle() {
        return feedbackBundle;
    }

    public String getIterationId() {
        return iterationId;
    }

    public String getState() {
        return state;
    }

    public String getTaskId() {
        return taskId;
    }
}
