package io.proleap.cobol.engine.parse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.proleap.cobol.asg.metamodel.Program;
import io.proleap.cobol.engine.ingest.SourceUnit;

public class ParseArtifact {

    private final SourceUnit sourceUnit;
    private final String compilationUnitName;
    private final Program program;
    private final List<String> sourceLines;
    private final List<String> diagnostics;

    public ParseArtifact(final SourceUnit sourceUnit, final String compilationUnitName, final Program program,
            final List<String> sourceLines, final List<String> diagnostics) {
        this.sourceUnit = Objects.requireNonNull(sourceUnit, "sourceUnit");
        this.compilationUnitName = Objects.requireNonNull(compilationUnitName, "compilationUnitName");
        this.program = Objects.requireNonNull(program, "program");
        this.sourceLines = sourceLines == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<String>(sourceLines));
        this.diagnostics = diagnostics == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<String>(diagnostics));
    }

    public String getCompilationUnitName() {
        return compilationUnitName;
    }

    public List<String> getDiagnostics() {
        return diagnostics;
    }

    public Program getProgram() {
        return program;
    }

    public List<String> getSourceLines() {
        return sourceLines;
    }

    public SourceUnit getSourceUnit() {
        return sourceUnit;
    }
}
