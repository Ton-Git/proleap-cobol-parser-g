package io.proleap.cobol.engine;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.junit.Test;

import io.proleap.cobol.engine.cli.MigrationCli;

public class MigrationCliTest {

    @Test
    public void shouldRunCliOnProgramRootAndWriteProgramArtifacts() throws Exception {
        final File fixture = new File("src/test/resources/io/proleap/cobol/ast/HelloWorld.cbl");
        final File programRoot = Files.createTempDirectory("migration-cli-program-root").toFile();
        Files.copy(fixture.toPath(), new File(programRoot, "HelloWorld.cbl").toPath(), StandardCopyOption.REPLACE_EXISTING);
        final File outputDir = Files.createTempDirectory("migration-cli-output").toFile();

        MigrationCli.main(new String[] { programRoot.getAbsolutePath(), outputDir.getAbsolutePath() });

        final File programDir = new File(outputDir, programRoot.getName());
        assertTrue(programDir.isDirectory());
        assertTrue(new File(programDir, "program-conversion-record.json").isFile());
        assertTrue(new File(programDir, "ir-slice-bundle.json").isFile());
        assertTrue(new File(programDir, "agent-task-contracts.json").isFile());
        assertTrue(new File(programDir, "human-handoff-packets.json").isFile());
    }
}
