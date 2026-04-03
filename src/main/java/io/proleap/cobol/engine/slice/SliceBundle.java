package io.proleap.cobol.engine.slice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class SliceBundle {

    private final String bundleId;
    private final String programName;
    private final List<ConversionSlice> slices;

    public SliceBundle(final String bundleId, final String programName, final List<ConversionSlice> slices) {
        this.bundleId = Objects.requireNonNull(bundleId, "bundleId");
        this.programName = Objects.requireNonNull(programName, "programName");
        this.slices = slices == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<ConversionSlice>(slices));
    }

    public String getBundleId() {
        return bundleId;
    }

    public String getProgramName() {
        return programName;
    }

    public List<ConversionSlice> getSlices() {
        return slices;
    }
}
