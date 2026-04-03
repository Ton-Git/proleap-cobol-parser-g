package io.proleap.cobol.engine.codegen;

import java.util.Objects;

public class GeneratedFile {

    private final String path;
    private final String content;

    public GeneratedFile(final String path, final String content) {
        this.path = Objects.requireNonNull(path, "path");
        this.content = Objects.requireNonNull(content, "content");
    }

    public String getContent() {
        return content;
    }

    public String getPath() {
        return path;
    }
}
