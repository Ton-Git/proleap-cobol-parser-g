package io.proleap.cobol.engine.slice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import io.proleap.cobol.engine.ir.IrStatement;

public class ConversionSlice {

    private final String sliceId;
    private final String sliceType;
    private final List<IrStatement> statements;
    private final List<String> acceptanceCriteria;

    public ConversionSlice(final String sliceId, final String sliceType, final List<IrStatement> statements,
            final List<String> acceptanceCriteria) {
        this.sliceId = Objects.requireNonNull(sliceId, "sliceId");
        this.sliceType = Objects.requireNonNull(sliceType, "sliceType");
        this.statements = statements == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<IrStatement>(statements));
        this.acceptanceCriteria = acceptanceCriteria == null ? Collections.emptyList()
                : Collections.unmodifiableList(new ArrayList<String>(acceptanceCriteria));
    }

    public List<String> getAcceptanceCriteria() {
        return acceptanceCriteria;
    }

    public String getSliceId() {
        return sliceId;
    }

    public List<IrStatement> getStatements() {
        return statements;
    }

    public String getSliceType() {
        return sliceType;
    }
}
