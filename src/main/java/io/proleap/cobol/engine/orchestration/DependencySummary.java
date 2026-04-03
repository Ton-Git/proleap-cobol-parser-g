package io.proleap.cobol.engine.orchestration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DependencySummary {

    private final String workspaceId;
    private final int sourceUnitCount;
    private final List<String> sourceFiles;
    private final List<String> copybookDirectories;

    public DependencySummary(final String workspaceId, final int sourceUnitCount, final List<String> sourceFiles,
            final List<String> copybookDirectories) {
        this.workspaceId = Objects.requireNonNull(workspaceId, "workspaceId");
        this.sourceUnitCount = sourceUnitCount;
        this.sourceFiles = sourceFiles == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(sourceFiles));
        this.copybookDirectories = copybookDirectories == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(copybookDirectories));
    }

    public List<String> getCopybookDirectories() {
        return copybookDirectories;
    }

    public int getSourceUnitCount() {
        return sourceUnitCount;
    }

    public List<String> getSourceFiles() {
        return sourceFiles;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }
}
