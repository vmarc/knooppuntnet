package kpn.server.analyzer.engine.monitor.reference;

import java.util.HashMap;
import java.util.Objects;

public abstract class Element {

    private HashMap<String, String> tags = new HashMap();

    boolean hasTag(String key, String... values) {
        final String actualValue = tags.get(key);
        for (String value : values) {
            if (Objects.equals(value, actualValue)) {
                return true;
            }
        }
        return false;
    }
}
