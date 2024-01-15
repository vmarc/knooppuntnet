package kpn.server.analyzer.engine.monitor.reference;

import java.util.HashMap;

public class Node extends Element {

    private final Long id;

    public Node(Long id) {
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
