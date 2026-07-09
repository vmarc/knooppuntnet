package kpn.server.analyzer.engine.analysis.route.structure.reference;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public abstract class JavaElement {

    private Map<String, String> tags = new HashMap<>();

    public JavaElement(final Map<String, String> tags) {
        this.tags = tags;
    }

    boolean hasTag(final String key, final String... values) {
        final String actualValue = tags.get(key);
        for (String value : values) {
            if (Objects.equals(value, actualValue)) {
                return true;
            }
        }
        return false;
    }
}
