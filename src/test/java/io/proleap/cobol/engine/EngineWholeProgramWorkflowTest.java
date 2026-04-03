package io.proleap.cobol.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Collections;

import org.junit.Test;

import io.proleap.cobol.engine.ingest.IngestionManifest;
import io.proleap.cobol.engine.ingest.ProgramWorkspace;
import io.proleap.cobol.engine.ingest.SourceUnit;
import io.proleap.cobol.engine.orchestration.ConversionEngine;
import io.proleap.cobol.engine.orchestration.ProgramConversionRecord;
import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

public class EngineWholeProgramWorkflowTest {

    @Test
    public void shouldConvertWholeProgramWorkspaceAndEmitHumanHandoffArtifacts() throws Exception {
        final File fixture = new File("src/test/resources/io/proleap/cobol/ast/HelloWorld.cbl");
        final File programRoot = Files.createTempDirectory("whole-program-root").toFile();
        final File copiedFixture = new File(programRoot, "HelloWorld.cbl");
        Files.copy(fixture.toPath(), copiedFixture.toPath(), StandardCopyOption.REPLACE_EXISTING);

        final SourceUnit sourceUnit = new SourceUnit(
                "hello-world",
                copiedFixture,
                CobolSourceFormatEnum.FIXED,
                StandardCharsets.UTF_8,
                Collections.singletonList(programRoot),
                "reference",
                "whole-program-test");
        final ProgramWorkspace workspace = new ProgramWorkspace(
                "legacy-main-program",
                programRoot,
                Collections.singletonList(sourceUnit),
                "reference",
                "whole-program-test");
        final IngestionManifest manifest = new IngestionManifest("manifest-whole-program", Collections.singletonList(workspace));

        final ProgramConversionRecord record = new ConversionEngine().convert(manifest).get(0);

        assertEquals("legacy-main-program", record.getProgramWorkspace().getWorkspaceId());
        assertEquals(1, record.getProgramWorkspace().getSourceUnits().size());
        assertEquals("LegacyMainProgram", record.getIrProgram().getProgramName());
        assertFalse(record.getGeneratedProject().getGeneratedFiles().isEmpty());
        assertEquals(1, record.getAgentTaskContracts().size());
        assertEquals("human-agent-bounded-implementation", record.getAgentTaskContracts().get(0).getTaskType());
        assertEquals(1, record.getHandoffPackets().size());
        assertTrue(record.getHandoffPackets().get(0).getInstructions().contains("Human developer"));
        assertTrue(record.getVerificationReport().isPassed());
    }
}
