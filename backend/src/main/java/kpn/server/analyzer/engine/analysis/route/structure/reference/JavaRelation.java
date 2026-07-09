package kpn.server.analyzer.engine.analysis.route.structure.reference;

import java.util.HashMap;
import java.util.List;

public class JavaRelation extends JavaElement {

    private final List<JavaMember> members;

    public JavaRelation(final List<JavaMember> members) {
        super(new HashMap<>());
        this.members = members;
    }

    public List<JavaMember> getMembers() {
        return members;
    }
}
