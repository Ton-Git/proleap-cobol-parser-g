package io.proleap.cobol.engine.ir;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class IrProgram {

    private final String schemaVersion;
    private final String programName;
    private final List<IrStatement> statements;
    private final List<String> diagnostics;

    public IrProgram(final String schemaVersion, final String programName, final List<IrStatement> statements,
            final List<String> diagnostics) {
        this.schemaVersion = Objects.requireNonNull(schemaVersion, "schemaVersion");
        this.programName = Objects.requireNonNull(programName, "programName");
        this.statements = statements == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<IrStatement>(statements));
        this.diagnostics = diagnostics == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<String>(diagnostics));
    }

    public List<String> getDiagnostics() {
        return diagnostics;
    }

    public String getProgramName() {
        return programName;
    }

    public String getSchemaVersion() {
        return schemaVersion;
    }

    public List<IrStatement> getStatements() {
        return statements;
    }
}
