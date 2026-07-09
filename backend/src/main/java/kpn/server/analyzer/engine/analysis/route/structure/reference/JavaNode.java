package kpn.server.analyzer.engine.analysis.route.structure.reference;

import java.util.HashMap;

public class JavaNode extends JavaElement {

    private final Long id;

    public JavaNode(Long id) {
        super(new HashMap<>());
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String toString() {
        return String.format("Node(%d)", id);
    }
}
