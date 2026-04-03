package io.proleap.cobol.engine.ir;

import java.util.Objects;

public class IrStatement {

    public enum Type {
        DISPLAY,
        STOP,
        UNSUPPORTED
    }

    private final Type type;
    private final String text;
    private final String provenance;

    public IrStatement(final Type type, final String text, final String provenance) {
        this.type = Objects.requireNonNull(type, "type");
        this.text = text == null ? "" : text;
        this.provenance = provenance == null ? "" : provenance;
    }

    public String getProvenance() {
        return provenance;
    }

    public String getText() {
        return text;
    }

    public Type getType() {
        return type;
    }
}
