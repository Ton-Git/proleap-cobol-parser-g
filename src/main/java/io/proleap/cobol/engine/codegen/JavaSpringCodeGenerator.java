package io.proleap.cobol.engine.codegen;

import java.util.ArrayList;
import java.util.List;

import io.proleap.cobol.engine.ir.IrProgram;
import io.proleap.cobol.engine.ir.IrStatement;

public class JavaSpringCodeGenerator {

    public GeneratedProject generate(final IrProgram program) {
        final String packageSuffix = sanitize(program.getProgramName()).toLowerCase();
        final String packageName = "io.proleap.generated." + packageSuffix;
        final String className = sanitize(program.getProgramName()) + "Program";
        final List<GeneratedFile> files = new ArrayList<GeneratedFile>();

        files.add(new GeneratedFile("src/main/java/" + packageName.replace('.', '/') + "/" + className + ".java",
                buildProgramClass(packageName, className, program)));
        files.add(new GeneratedFile("src/main/java/" + packageName.replace('.', '/') + "/GeneratedApplication.java",
                buildApplicationClass(packageName, className)));
        files.add(new GeneratedFile("pom.xml", buildPom(program.getProgramName())));
        files.add(new GeneratedFile("src/main/resources/application.properties",
                "spring.application.name=" + packageSuffix + "\n"));

        return new GeneratedProject(program.getProgramName(), files);
    }

    private String buildApplicationClass(final String packageName, final String className) {
        return "package " + packageName + ";\n\n"
                + "import io.proleap.cobol.engine.runtime.CobolRuntime;\n\n"
                + "public class GeneratedApplication {\n\n"
                + "    public static void main(final String[] args) {\n"
                + "        final CobolRuntime runtime = new CobolRuntime();\n"
                + "        new " + className + "().run(runtime);\n"
                + "    }\n"
                + "}\n";
    }

    private String buildPom(final String programName) {
        return "<project xmlns=\"http://maven.apache.org/POM/4.0.0\"\n"
                + "         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                + "         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd\">\n"
                + "  <modelVersion>4.0.0</modelVersion>\n"
                + "  <groupId>io.proleap.generated</groupId>\n"
                + "  <artifactId>" + sanitize(programName).toLowerCase() + "</artifactId>\n"
                + "  <version>1.0.0-SNAPSHOT</version>\n"
                + "  <packaging>jar</packaging>\n"
                + "</project>\n";
    }

    private String buildProgramClass(final String packageName, final String className, final IrProgram program) {
        final StringBuilder builder = new StringBuilder();
        builder.append("package ").append(packageName).append(";\n\n");
        builder.append("import io.proleap.cobol.engine.runtime.CobolRuntime;\n\n");
        builder.append("public class ").append(className).append(" {\n\n");
        builder.append("    public void run(final CobolRuntime runtime) {\n");
        for (final IrStatement statement : program.getStatements()) {
            if (statement.getType() == IrStatement.Type.DISPLAY) {
                builder.append("        runtime.display(\"").append(escape(statement.getText())).append("\");\n");
            } else if (statement.getType() == IrStatement.Type.STOP) {
                builder.append("        runtime.stop();\n");
            } else {
                builder.append("        // TODO unsupported statement: ").append(escape(statement.getText())).append("\n");
            }
        }
        builder.append("    }\n");
        builder.append("}\n");
        return builder.toString();
    }

    private String escape(final String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private String sanitize(final String input) {
        final String candidate = input == null ? "Program" : input.replaceAll("[^A-Za-z0-9]", "");
        return candidate.isEmpty() ? "Program" : Character.toUpperCase(candidate.charAt(0)) + candidate.substring(1);
    }
}
