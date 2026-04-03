package io.proleap.cobol.engine.ingest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class IngestionManifest {

    private final String manifestId;
    private final List<ProgramWorkspace> programWorkspaces;

    public IngestionManifest(final String manifestId, final List<ProgramWorkspace> programWorkspaces) {
        this.manifestId = Objects.requireNonNull(manifestId, "manifestId");
        this.programWorkspaces = programWorkspaces == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<ProgramWorkspace>(programWorkspaces));
    }

    public String getManifestId() {
        return manifestId;
    }

    public List<ProgramWorkspace> getProgramWorkspaces() {
        return programWorkspaces;
    }
}
