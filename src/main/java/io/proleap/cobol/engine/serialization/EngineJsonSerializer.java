package io.proleap.cobol.engine.serialization;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class EngineJsonSerializer {

    public String toJson(final Object value) {
        if (value == null) {
            return "null";
        }
        if (value instanceof String) {
            return quote((String) value);
        }
        if (value instanceof Number || value instanceof Boolean) {
            return String.valueOf(value);
        }
        if (value instanceof Map) {
            return writeMap((Map<String, Object>) value);
        }
        if (value instanceof List) {
            return writeList((List<Object>) value);
        }
        return quote(String.valueOf(value));
    }

    private String quote(final String value) {
        return "\"" + value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t")
                + "\"";
    }

    private String writeList(final List<Object> values) {
        final StringBuilder builder = new StringBuilder();
        builder.append("[");
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                builder.append(", ");
            }
            builder.append(toJson(values.get(i)));
        }
        builder.append("]");
        return builder.toString();
    }

    private String writeMap(final Map<String, Object> values) {
        final StringBuilder builder = new StringBuilder();
        builder.append("{");
        final Iterator<Map.Entry<String, Object>> iterator = values.entrySet().iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, Object> entry = iterator.next();
            builder.append(quote(entry.getKey())).append(": ").append(toJson(entry.getValue()));
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }
}
