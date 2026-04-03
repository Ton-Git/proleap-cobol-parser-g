package io.proleap.cobol.engine.cli;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.proleap.cobol.engine.ingest.IngestionManifest;
import io.proleap.cobol.engine.ingest.ProgramWorkspace;
import io.proleap.cobol.engine.ingest.SourceUnit;
import io.proleap.cobol.engine.orchestration.ConversionEngine;
import io.proleap.cobol.engine.orchestration.ProgramConversionRecord;
import io.proleap.cobol.engine.serialization.EngineArtifactWriter;
import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

public class MigrationCli {

    public static void main(final String[] args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Usage: MigrationCli <program-root> <output-dir>");
        }

        final File programRoot = new File(args[0]);
        final File outputDir = new File(args[1]);
        final List<SourceUnit> sourceUnits = discoverSourceUnits(programRoot);
        final ProgramWorkspace workspace = new ProgramWorkspace(programRoot.getName(), programRoot, sourceUnits, "cli", "manual");
        final IngestionManifest manifest = new IngestionManifest("cli-manifest", Collections.singletonList(workspace));
        final List<ProgramConversionRecord> records = new ConversionEngine().convert(manifest);
        final EngineArtifactWriter writer = new EngineArtifactWriter();
        for (final ProgramConversionRecord record : records) {
            final File programDir = new File(outputDir, record.getProgramWorkspace().getWorkspaceId());
            writer.write(programDir, record);
        }
    }

    private static List<SourceUnit> discoverSourceUnits(final File programRoot) throws Exception {
        final List<SourceUnit> sourceUnits = new ArrayList<SourceUnit>();
        Files.walk(programRoot.toPath())
                .filter(Files::isRegularFile)
                .filter(path -> isCobolSource(path.toFile().getName()))
                .forEach(path -> sourceUnits.add(new SourceUnit(
                        path.getFileName().toString(),
                        path.toFile(),
                        CobolSourceFormatEnum.FIXED,
                        StandardCharsets.UTF_8,
                        Collections.singletonList(programRoot),
                        "cli",
                        "manual")));
        if (sourceUnits.isEmpty()) {
            throw new IllegalArgumentException("No COBOL source files found under program root: " + programRoot.getAbsolutePath());
        }
        return sourceUnits;
    }

    private static boolean isCobolSource(final String name) {
        final String lower = name.toLowerCase();
        return lower.endsWith(".cbl") || lower.endsWith(".cob") || lower.endsWith(".cobol");
    }
}
