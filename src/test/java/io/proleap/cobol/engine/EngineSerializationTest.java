package io.proleap.cobol.engine;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

import org.junit.Test;

import io.proleap.cobol.engine.ingest.IngestionManifest;
import io.proleap.cobol.engine.ingest.ProgramWorkspace;
import io.proleap.cobol.engine.ingest.SourceUnit;
import io.proleap.cobol.engine.orchestration.ConversionEngine;
import io.proleap.cobol.engine.orchestration.ProgramConversionRecord;
import io.proleap.cobol.engine.serialization.EngineArtifactWriter;
import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

public class EngineSerializationTest {

    @Test
    public void shouldWriteWholeProgramArtifactsAsJsonFiles() throws Exception {
        final File fixture = new File("src/test/resources/io/proleap/cobol/ast/HelloWorld.cbl");
        final SourceUnit sourceUnit = new SourceUnit(
                "hello-world",
                fixture,
                CobolSourceFormatEnum.FIXED,
                StandardCharsets.UTF_8,
                Collections.singletonList(fixture.getParentFile()),
                "reference",
                "serialization-test");
        final ProgramWorkspace workspace = new ProgramWorkspace(
                "legacy-main-program",
                fixture.getParentFile(),
                Collections.singletonList(sourceUnit),
                "reference",
                "serialization-test");
        final ProgramConversionRecord record = new ConversionEngine()
                .convert(new IngestionManifest("manifest-serialization", Collections.singletonList(workspace))).get(0);

        final File outputDir = Files.createTempDirectory("engine-artifacts").toFile();
        new EngineArtifactWriter().write(outputDir, record);

        assertTrue(new File(outputDir, "program-conversion-record.json").isFile());
        assertTrue(new File(outputDir, "ir-slice-bundle.json").isFile());
        assertTrue(new File(outputDir, "agent-task-contracts.json").isFile());
        assertTrue(new File(outputDir, "human-handoff-packets.json").isFile());

        final String recordJson = Files.readString(new File(outputDir, "program-conversion-record.json").toPath());
        final String sliceJson = Files.readString(new File(outputDir, "ir-slice-bundle.json").toPath());
        final String taskJson = Files.readString(new File(outputDir, "agent-task-contracts.json").toPath());
        final String handoffJson = Files.readString(new File(outputDir, "human-handoff-packets.json").toPath());

        assertTrue(recordJson.contains("\"workspace_id\": \"legacy-main-program\""));
        assertTrue(recordJson.contains("\"status\": \"accepted\""));
        assertTrue(sliceJson.contains("\"slice_id\": \"legacymainprogram-slice-1\""));
        assertTrue(taskJson.contains("\"task_type\": \"human-agent-bounded-implementation\""));
        assertTrue(handoffJson.contains("\"instructions\": \"Human developer chooses any coding agent"));
    }
}
