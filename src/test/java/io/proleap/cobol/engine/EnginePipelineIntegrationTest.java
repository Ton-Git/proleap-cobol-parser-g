package io.proleap.cobol.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.junit.Test;

import io.proleap.cobol.engine.ingest.IngestionManifest;
import io.proleap.cobol.engine.ingest.ProgramWorkspace;
import io.proleap.cobol.engine.ingest.SourceUnit;
import io.proleap.cobol.engine.orchestration.ConversionEngine;
import io.proleap.cobol.engine.orchestration.ProgramConversionRecord;
import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

public class EnginePipelineIntegrationTest {

    @Test
    public void shouldExecuteDeterministicConversionLoopForWholeProgramWorkspace() throws Exception {
        final File fixture = new File("src/test/resources/io/proleap/cobol/ast/HelloWorld.cbl");
        final SourceUnit sourceUnit = new SourceUnit(
                "hello-world",
                fixture,
                CobolSourceFormatEnum.FIXED,
                StandardCharsets.UTF_8,
                Collections.singletonList(fixture.getParentFile()),
                "reference",
                "integration-test");
        final ProgramWorkspace workspace = new ProgramWorkspace(
                "legacy-main-program",
                fixture.getParentFile(),
                Collections.singletonList(sourceUnit),
                "reference",
                "integration-test");
        final IngestionManifest manifest = new IngestionManifest("manifest-hello-world", Collections.singletonList(workspace));

        final ConversionEngine engine = new ConversionEngine();
        final ProgramConversionRecord record = engine.convert(manifest).get(0);

        assertEquals("LegacyMainProgram", record.getIrProgram().getProgramName());
        assertEquals(1, record.getSliceBundle().getSlices().size());
        assertFalse(record.getGeneratedProject().getGeneratedFiles().isEmpty());
        assertTrue(record.getGeneratedProject().findFile("src/main/java/io/proleap/generated/legacymainprogram/LegacyMainProgramProgram.java").isPresent());
        assertTrue(record.getGeneratedProject().findFile("src/main/java/io/proleap/generated/legacymainprogram/GeneratedApplication.java").isPresent());
        assertTrue(record.getGeneratedProject().findFile("src/main/java/io/proleap/generated/legacymainprogram/LegacyMainProgramProgram.java").get().getContent().contains("runtime.display(\"Hello world\")"));
        assertTrue(record.getVerificationReport().isPassed());
        assertEquals(ProgramConversionRecord.Status.ACCEPTED, record.getStatus());
        assertEquals(1, record.getAgentTaskContracts().size());
        assertEquals(1, record.getHandoffPackets().size());
    }
}
