package io.proleap.cobol.engine.ingest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ProgramWorkspace {

    private final String workspaceId;
    private final File rootDirectory;
    private final List<SourceUnit> sourceUnits;
    private final String corpus;
    private final String origin;

    public ProgramWorkspace(final String workspaceId, final File rootDirectory, final List<SourceUnit> sourceUnits,
            final String corpus, final String origin) {
        this.workspaceId = Objects.requireNonNull(workspaceId, "workspaceId");
        this.rootDirectory = Objects.requireNonNull(rootDirectory, "rootDirectory");
        this.sourceUnits = sourceUnits == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<SourceUnit>(sourceUnits));
        this.corpus = corpus == null ? "default" : corpus;
        this.origin = origin == null ? "unknown" : origin;
    }

    public String getCorpus() {
        return corpus;
    }

    public String getOrigin() {
        return origin;
    }

    public File getRootDirectory() {
        return rootDirectory;
    }

    public List<SourceUnit> getSourceUnits() {
        return sourceUnits;
    }

    public String getWorkspaceId() {
        return workspaceId;
    }
}
