package io.proleap.cobol.engine.verification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VerificationReport {

    private final boolean passed;
    private final List<String> checks;
    private final List<String> mismatches;

    public VerificationReport(final boolean passed, final List<String> checks, final List<String> mismatches) {
        this.passed = passed;
        this.checks = checks == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<String>(checks));
        this.mismatches = mismatches == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList<String>(mismatches));
    }

    public List<String> getChecks() {
        return checks;
    }

    public List<String> getMismatches() {
        return mismatches;
    }

    public boolean isPassed() {
        return passed;
    }
}
