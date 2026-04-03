package io.proleap.cobol.engine;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.junit.Test;

import io.proleap.cobol.engine.codegen.GeneratedProject;
import io.proleap.cobol.engine.codegen.JavaSpringCodeGenerator;
import io.proleap.cobol.engine.ingest.ProgramWorkspace;
import io.proleap.cobol.engine.ingest.SourceUnit;
import io.proleap.cobol.engine.ir.IrProgram;
import io.proleap.cobol.engine.ir.IrProgramBuilder;
import io.proleap.cobol.engine.parse.ParseArtifact;
import io.proleap.cobol.engine.parse.ParseArtifactService;
import io.proleap.cobol.engine.slice.IrSlicePackager;
import io.proleap.cobol.engine.slice.SliceBundle;
import io.proleap.cobol.engine.verification.VerificationHarness;
import io.proleap.cobol.engine.verification.VerificationReport;
import io.proleap.cobol.preprocessor.CobolPreprocessor.CobolSourceFormatEnum;

public class EngineArtifactsTest {

    @Test
    public void shouldCreateIrSliceBundleGeneratedProjectAndVerificationEvidence() throws Exception {
        final File fixture = new File("src/test/resources/io/proleap/cobol/ast/HelloWorld.cbl");
        final SourceUnit sourceUnit = new SourceUnit(
                "hello-world",
                fixture,
                CobolSourceFormatEnum.FIXED,
                StandardCharsets.UTF_8,
                Collections.singletonList(fixture.getParentFile()),
                "reference",
                "artifact-test");

        final ParseArtifact parseArtifact = new ParseArtifactService().parse(sourceUnit);
        final IrProgram unitProgram = new IrProgramBuilder().build(parseArtifact);
        final ProgramWorkspace workspace = new ProgramWorkspace(
                "legacy-main-program",
                fixture.getParentFile(),
                Collections.singletonList(sourceUnit),
                "reference",
                "artifact-test");
        final IrProgram irProgram = new IrProgram("v1", "LegacyMainProgram", unitProgram.getStatements(), unitProgram.getDiagnostics());
        final SliceBundle bundle = new IrSlicePackager().packageProgram(irProgram);
        final GeneratedProject project = new JavaSpringCodeGenerator().generate(irProgram);
        final VerificationReport verificationReport = new VerificationHarness().verify(irProgram, project);

        assertEquals("legacy-main-program", workspace.getWorkspaceId());
        assertEquals("v1", irProgram.getSchemaVersion());
        assertEquals(2, irProgram.getStatements().size());
        assertEquals(1, bundle.getSlices().size());
        assertTrue(bundle.getSlices().get(0).getAcceptanceCriteria().contains("compile-generated-java"));
        assertTrue(project.findFile("pom.xml").isPresent());
        assertTrue(project.findFile("src/main/resources/application.properties").isPresent());
        assertTrue(verificationReport.isPassed());
        assertTrue(verificationReport.getChecks().contains("generated-program-file-present"));
    }
}
