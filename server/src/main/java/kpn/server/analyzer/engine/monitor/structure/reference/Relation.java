package kpn.server.analyzer.engine.monitor.structure.reference;

import java.util.HashMap;
import java.util.List;

public class Relation extends Element {

    private final List<Member> members;

    public Relation(final List<Member> members) {
        super(new HashMap<>());
        this.members = members;
    }

    public List<Member> getMembers() {
        return members;
    }
}
