package io.proleap.cobol.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import io.proleap.cobol.engine.ingest.IngestionManifest;
import io.proleap.cobol.engine.ingest.ProgramWorkspace;
import io.proleap.cobol.engine.ingest.SourceUnit;
import io.proleap.cobol.engine.orchestration.ConversionEngine;
import io.proleap.cobol.engine.orchestration.ProgramConversionRecord;
import io.proleap.cobol.engine.serialization.EngineArtifactWriter;
import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

public class EngineExtendedWorkflowArtifactsTest {

    @Test
    public void shouldWriteConcreteHandoffPacketDirectoriesGeneratedProjectAndDependencyArtifacts() throws Exception {
        final File fixture = new File("src/test/resources/io/proleap/cobol/ast/HelloWorld.cbl");
        final SourceUnit sourceUnit = new SourceUnit(
                "hello-world",
                fixture,
                CobolSourceFormatEnum.FIXED,
                StandardCharsets.UTF_8,
                Collections.singletonList(fixture.getParentFile()),
                "reference",
                "extended-artifacts-test");
        final ProgramWorkspace workspace = new ProgramWorkspace(
                "legacy-main-program",
                fixture.getParentFile(),
                Collections.singletonList(sourceUnit),
                "reference",
                "extended-artifacts-test");
        final ProgramConversionRecord record = new ConversionEngine()
                .convert(new IngestionManifest("manifest-extended-artifacts", Collections.singletonList(workspace))).get(0);

        final File outputDir = Files.createTempDirectory("engine-extended-artifacts").toFile();
        new EngineArtifactWriter().write(outputDir, record);

        assertTrue(new File(outputDir, "generated-project/pom.xml").isFile());
        assertTrue(new File(outputDir,
                "generated-project/src/main/java/io/proleap/generated/legacymainprogram/LegacyMainProgramProgram.java").isFile());
        assertTrue(new File(outputDir, "handoff-packets/legacymainprogram-slice-1/instructions.md").isFile());
        assertTrue(new File(outputDir, "handoff-packets/legacymainprogram-slice-1/task-contract.json").isFile());
        assertTrue(new File(outputDir, "handoff-packets/legacymainprogram-slice-1/submission-checklist.md").isFile());
        assertTrue(new File(outputDir, "dependency-summary.json").isFile());
        assertTrue(new File(outputDir, "integration-checkpoints.json").isFile());
    }

    @Test
    public void shouldCreateMultipleSlicesAndIntegrationCheckpointForMultiSourceWorkspace() throws Exception {
        final File fixture = new File("src/test/resources/io/proleap/cobol/ast/HelloWorld.cbl");
        final File programRoot = Files.createTempDirectory("multi-source-program-root").toFile();
        final File first = new File(programRoot, "part-one.cbl");
        final File second = new File(programRoot, "part-two.cbl");
        Files.copy(fixture.toPath(), first.toPath(), StandardCopyOption.REPLACE_EXISTING);
        Files.copy(fixture.toPath(), second.toPath(), StandardCopyOption.REPLACE_EXISTING);

        final SourceUnit firstUnit = new SourceUnit(
                "part-one",
                first,
                CobolSourceFormatEnum.FIXED,
                StandardCharsets.UTF_8,
                Collections.singletonList(programRoot),
                "reference",
                "multi-source-test");
        final SourceUnit secondUnit = new SourceUnit(
                "part-two",
                second,
                CobolSourceFormatEnum.FIXED,
                StandardCharsets.UTF_8,
                Collections.singletonList(programRoot),
                "reference",
                "multi-source-test");
        final ProgramWorkspace workspace = new ProgramWorkspace(
                "legacy-main-program",
                programRoot,
                Arrays.asList(firstUnit, secondUnit),
                "reference",
                "multi-source-test");

        final ProgramConversionRecord record = new ConversionEngine()
                .convert(new IngestionManifest("manifest-multi-source", Collections.singletonList(workspace))).get(0);

        assertEquals(2, record.getParseArtifacts().size());
        assertEquals(4, record.getIrProgram().getStatements().size());
        assertEquals(2, record.getSliceBundle().getSlices().size());
        assertEquals(2, record.getHandoffPackets().size());
        assertEquals(1, record.getIntegrationCheckpoints().size());
        assertEquals("program_integration_ready", record.getConvergenceState());
    }
}
