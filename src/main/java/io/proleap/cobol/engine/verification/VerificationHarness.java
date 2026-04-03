package io.proleap.cobol.engine.verification;

import java.util.ArrayList;
import java.util.List;

import io.proleap.cobol.engine.codegen.GeneratedFile;
import io.proleap.cobol.engine.codegen.GeneratedProject;
import io.proleap.cobol.engine.ir.IrProgram;
import io.proleap.cobol.engine.ir.IrStatement;

public class VerificationHarness {

    public VerificationReport verify(final IrProgram program, final GeneratedProject generatedProject) {
        final List<String> checks = new ArrayList<String>();
        final List<String> mismatches = new ArrayList<String>();
        final String path = "src/main/java/io/proleap/generated/" + program.getProgramName().toLowerCase() + "/"
                + program.getProgramName() + "Program.java";
        final GeneratedFile programFile = generatedProject.findFile(path).orElse(null);

        if (programFile == null) {
            mismatches.add("missing-generated-program-file");
            return new VerificationReport(false, checks, mismatches);
        }

        final String content = programFile.getContent();
        checks.add("generated-program-file-present");

        for (final IrStatement statement : program.getStatements()) {
            if (statement.getType() == IrStatement.Type.DISPLAY) {
                final String needle = "runtime.display(\"" + escape(statement.getText()) + "\")";
                if (content.contains(needle)) {
                    checks.add("display-mapped:" + statement.getText());
                } else {
                    mismatches.add("missing-display:" + statement.getText());
                }
            }
            if (statement.getType() == IrStatement.Type.STOP) {
                if (content.contains("runtime.stop();")) {
                    checks.add("stop-mapped");
                } else {
                    mismatches.add("missing-stop");
                }
            }
        }

        return new VerificationReport(mismatches.isEmpty(), checks, mismatches);
    }

    private String escape(final String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
