package io.proleap.cobol.engine.codegen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class GeneratedProject {

    private final String projectName;
    private final List<GeneratedFile> generatedFiles;

    public GeneratedProject(final String projectName, final List<GeneratedFile> generatedFiles) {
        this.projectName = Objects.requireNonNull(projectName, "projectName");
        this.generatedFiles = generatedFiles == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<GeneratedFile>(generatedFiles));
    }

    public Optional<GeneratedFile> findFile(final String path) {
        for (final GeneratedFile file : generatedFiles) {
            if (file.getPath().equals(path)) {
                return Optional.of(file);
            }
        }
        return Optional.empty();
    }

    public List<GeneratedFile> getGeneratedFiles() {
        return generatedFiles;
    }

    public String getProjectName() {
        return projectName;
    }
}
