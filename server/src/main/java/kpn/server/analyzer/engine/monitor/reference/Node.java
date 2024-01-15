package kpn.server.analyzer.engine.monitor.reference;

public class Node extends Element {

    private final Long id;

    public Node(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String toString() {
        return String.format("Node(%d)", id);
    }
}
