package io.proleap.cobol.engine.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CobolRuntime {

    private final List<String> displayOutputs = new ArrayList<String>();
    private boolean stopped;

    public void display(final String text) {
        displayOutputs.add(text);
    }

    public List<String> getDisplayOutputs() {
        return Collections.unmodifiableList(displayOutputs);
    }

    public boolean isStopped() {
        return stopped;
    }

    public void stop() {
        this.stopped = true;
    }
}
