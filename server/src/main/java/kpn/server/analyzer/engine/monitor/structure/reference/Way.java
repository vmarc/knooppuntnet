package kpn.server.analyzer.engine.monitor.structure.reference;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Way extends Element {

    private final Long id;
    private final List<Node> nodes;

    public Way(final Long id, final Map<String, String> tags, final List<Node> nodes) {
        super(tags);
        this.id = id;
        this.nodes = nodes;
    }

    public Long getId() {
        return id;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public int getNodesCount() {
        return nodes.size();
    }

    public Node getNode(final int index) {
        return nodes.get(index);
    }

    public int isOneway() {
        // TODO look at tags to determine whether this is a oneway way
        return 0;
    }

    public Node firstNode(final boolean respectOneway) {
        return !respectOneway || isOneway() != -1 ? firstNode() : lastNode();
    }

    public Node lastNode(final boolean respectOneway) {
        return !respectOneway || isOneway() != -1 ? lastNode() : firstNode();
    }

    public Node lastNode() {
        if (nodes.isEmpty()) return null;
        return nodes.get(nodes.size() - 1);
    }

    public Node firstNode() {
        if (nodes.isEmpty()) return null;
        return nodes.get(0);
    }

    public boolean isFirstLastNode(final Node n) {
        if (nodes.isEmpty()) return false;
        return n == nodes.get(0) || n == nodes.get(nodes.size() - 1);
    }

    public boolean isClosed() {
        return nodes.size() >= 3 && nodes.get(nodes.size() - 1) == nodes.get(0);
    }

    public String toString() {
        String nodeIds = nodes.stream().map(node -> node.getId().toString()).collect(Collectors.joining(", ", "(", ")"));
        return String.format("Way(id=%d, nodeIds=%s)", id, nodeIds);
    }
}