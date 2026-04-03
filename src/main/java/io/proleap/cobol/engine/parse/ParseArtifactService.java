package io.proleap.cobol.engine.parse;

import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import io.proleap.cobol.asg.metamodel.CompilationUnit;
import io.proleap.cobol.asg.metamodel.Program;
import io.proleap.cobol.asg.params.CobolParserParams;
import io.proleap.cobol.asg.params.impl.CobolParserParamsImpl;
import io.proleap.cobol.asg.runner.impl.CobolParserRunnerImpl;
import io.proleap.cobol.engine.ingest.SourceUnit;

public class ParseArtifactService {

    public ParseArtifact parse(final SourceUnit sourceUnit) throws IOException {
        final CobolParserParams params = new CobolParserParamsImpl();
        params.setCharset(sourceUnit.getCharset());
        params.setFormat(sourceUnit.getSourceFormat());
        params.setCopyBookDirectories(sourceUnit.getCopybookDirectories());

        final Program program = new CobolParserRunnerImpl().analyzeFile(sourceUnit.getSourceFile(), params);
        final CompilationUnit compilationUnit = program.getCompilationUnit();
        final String compilationUnitName = compilationUnit != null ? compilationUnit.getName() : fallbackName(sourceUnit);
        final List<String> sourceLines = Files.readAllLines(sourceUnit.getSourceFile().toPath(), sourceUnit.getCharset());

        return new ParseArtifact(sourceUnit, compilationUnitName, program, sourceLines, new ArrayList<String>());
    }

    private String fallbackName(final SourceUnit sourceUnit) {
        final String fileName = sourceUnit.getSourceFile().getName();
        final int extensionIndex = fileName.lastIndexOf('.');
        final String stem = extensionIndex > 0 ? fileName.substring(0, extensionIndex) : fileName;
        return stem.isEmpty() ? sourceUnit.getSourceId() : Character.toUpperCase(stem.charAt(0)) + stem.substring(1);
    }
}
