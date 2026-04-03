package io.proleap.cobol.engine.agent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class HandoffPacket {

    private final String packetId;
    private final String taskId;
    private final String sliceId;
    private final String programId;
    private final String instructions;
    private final List<String> artifactRefs;
    private final List<String> submissionChecklist;

    public HandoffPacket(final String packetId, final String taskId, final String sliceId, final String programId,
            final String instructions, final List<String> artifactRefs, final List<String> submissionChecklist) {
        this.packetId = Objects.requireNonNull(packetId, "packetId");
        this.taskId = Objects.requireNonNull(taskId, "taskId");
        this.sliceId = Objects.requireNonNull(sliceId, "sliceId");
        this.programId = Objects.requireNonNull(programId, "programId");
        this.instructions = Objects.requireNonNull(instructions, "instructions");
        this.artifactRefs = artifactRefs == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(artifactRefs));
        this.submissionChecklist = submissionChecklist == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(submissionChecklist));
    }

    public List<String> getArtifactRefs() {
        return artifactRefs;
    }

    public String getInstructions() {
        return instructions;
    }

    public String getPacketId() {
        return packetId;
    }

    public String getProgramId() {
        return programId;
    }

    public String getSliceId() {
        return sliceId;
    }

    public List<String> getSubmissionChecklist() {
        return submissionChecklist;
    }

    public String getTaskId() {
        return taskId;
    }
}
