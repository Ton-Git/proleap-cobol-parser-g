package io.proleap.cobol.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.junit.Test;

import io.proleap.cobol.engine.ingest.IngestionManifest;
import io.proleap.cobol.engine.ingest.ProgramWorkspace;
import io.proleap.cobol.engine.ingest.SourceUnit;
import io.proleap.cobol.engine.parse.ParseArtifact;
import io.proleap.cobol.engine.parse.ParseArtifactService;
import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

public class EngineManifestAndParserTest {

    @Test
    public void shouldBuildWholeProgramManifestAndParseHelloWorldFixture() throws Exception {
        final File fixture = new File("src/test/resources/io/proleap/cobol/ast/HelloWorld.cbl");
        final SourceUnit sourceUnit = new SourceUnit(
                "hello-world",
                fixture,
                CobolSourceFormatEnum.FIXED,
                StandardCharsets.UTF_8,
                Collections.singletonList(fixture.getParentFile()),
                "reference",
                "test");
        final ProgramWorkspace workspace = new ProgramWorkspace(
                "legacy-main-program",
                fixture.getParentFile(),
                Collections.singletonList(sourceUnit),
                "reference",
                "test");
        final IngestionManifest manifest = new IngestionManifest("manifest-1", Collections.singletonList(workspace));

        final ParseArtifactService service = new ParseArtifactService();
        final ParseArtifact artifact = service.parse(sourceUnit);

        assertEquals("manifest-1", manifest.getManifestId());
        assertEquals(1, manifest.getProgramWorkspaces().size());
        assertEquals("legacy-main-program", manifest.getProgramWorkspaces().get(0).getWorkspaceId());
        assertNotNull(artifact.getProgram());
        assertEquals("HelloWorld", artifact.getCompilationUnitName());
        assertFalse(artifact.getSourceLines().isEmpty());
        assertTrue(artifact.getDiagnostics().isEmpty());
    }
}
