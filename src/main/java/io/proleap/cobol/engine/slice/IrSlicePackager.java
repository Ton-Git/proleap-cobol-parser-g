package io.proleap.cobol.engine.slice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.proleap.cobol.engine.ir.IrProgram;
import io.proleap.cobol.engine.ir.IrStatement;

public class IrSlicePackager {

    public SliceBundle packageProgram(final IrProgram program) {
        final List<ConversionSlice> slices = new ArrayList<ConversionSlice>();
        final List<IrStatement> statements = program.getStatements();
        final int sliceSize = Math.max(1, statements.size() / Math.max(1, Math.min(2, statements.size() / 2 + statements.size() % 2)));
        int index = 0;
        int sliceNumber = 1;
        while (index < statements.size()) {
            final int endExclusive = Math.min(statements.size(), index + Math.max(1, sliceSize));
            final List<IrStatement> sliceStatements = new ArrayList<IrStatement>(statements.subList(index, endExclusive));
            slices.add(new ConversionSlice(program.getProgramName().toLowerCase() + "-slice-" + sliceNumber,
                    sliceNumber == 1 ? "program-main-flow" : "program-follow-on-flow",
                    sliceStatements,
                    Arrays.asList("compile-generated-java", "verify-display-output")));
            index = endExclusive;
            sliceNumber++;
        }
        return new SliceBundle(program.getProgramName().toLowerCase() + "-bundle", program.getProgramName(), slices);
    }
}
