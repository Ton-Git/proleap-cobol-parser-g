package io.proleap.cobol.engine.ingest;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

public class SourceUnit {

    private final String sourceId;
    private final File sourceFile;
    private final CobolSourceFormatEnum sourceFormat;
    private final Charset charset;
    private final List<File> copybookDirectories;
    private final String corpus;
    private final String origin;

    public SourceUnit(final String sourceId, final File sourceFile, final CobolSourceFormatEnum sourceFormat,
            final Charset charset, final List<File> copybookDirectories, final String corpus, final String origin) {
        this.sourceId = Objects.requireNonNull(sourceId, "sourceId");
        this.sourceFile = Objects.requireNonNull(sourceFile, "sourceFile");
        this.sourceFormat = Objects.requireNonNull(sourceFormat, "sourceFormat");
        this.charset = Objects.requireNonNull(charset, "charset");
        this.copybookDirectories = copybookDirectories == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<File>(copybookDirectories));
        this.corpus = corpus == null ? "default" : corpus;
        this.origin = origin == null ? "unknown" : origin;
    }

    public Charset getCharset() {
        return charset;
    }

    public List<File> getCopybookDirectories() {
        return copybookDirectories;
    }

    public String getCorpus() {
        return corpus;
    }

    public String getOrigin() {
        return origin;
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public CobolSourceFormatEnum getSourceFormat() {
        return sourceFormat;
    }

    public String getSourceId() {
        return sourceId;
    }
}
