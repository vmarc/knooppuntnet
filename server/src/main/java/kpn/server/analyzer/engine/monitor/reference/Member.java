package kpn.server.analyzer.engine.monitor.reference;

import java.util.Objects;

public class Member {

    private final String role;

    private final Element member;

    public String getRole() {
        return role;
    }

    boolean hasRole() {
        return !"".equals(getRole());
    }

    public boolean isRelation() {
        return member instanceof Relation;
    }

    public boolean isWay() {
        return member instanceof Way;
    }

    public boolean isNode() {
        return member instanceof Node;
    }

    public Relation getRelation() {
        return (Relation) member;
    }

    public Way getWay() {
        return (Way) member;
    }

    public Node getNode() {
        return (Node) member;
    }

    public Element getMember() {
        return member;
    }

    public Member(String role, Element member) {
        this.role = role;
        this.member = member;
    }

    public String toString() {
        return "Member(" + member + ", role=\"" + role + "\")";
    }

    public int hashCode() {
        return Objects.hash(role, member);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Member that = (Member) obj;
        return Objects.equals(role, that.role) &&
                Objects.equals(member, that.member);
    }
}
