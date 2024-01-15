package kpn.server.analyzer.engine.monitor.reference;

import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toUnmodifiableList;

public class Relation extends Element {

    private final List<Member> members;

    public Relation(final Collection<Member> members) {
        this.members = members.stream().collect(toUnmodifiableList());
    }

    public List<Member> getMembers() {
        return members;
    }
}
