package io.proleap.cobol.engine.orchestration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class IntegrationCheckpoint {

    private final String checkpointId;
    private final String state;
    private final List<String> sliceIds;

    public IntegrationCheckpoint(final String checkpointId, final String state, final List<String> sliceIds) {
        this.checkpointId = Objects.requireNonNull(checkpointId, "checkpointId");
        this.state = Objects.requireNonNull(state, "state");
        this.sliceIds = sliceIds == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(sliceIds));
    }

    public String getCheckpointId() {
        return checkpointId;
    }

    public List<String> getSliceIds() {
        return sliceIds;
    }

    public String getState() {
        return state;
    }
}
