package kpn.server.analyzer.engine.analysis.route.structure.reference;

import java.util.Objects;

public class JavaMember {

    private final String role;

    private final JavaElement member;

    public String getRole() {
        return role;
    }

    boolean hasRole() {
        return !"".equals(getRole());
    }

    public boolean isRelation() {
        return member instanceof JavaRelation;
    }

    public boolean isWay() {
        return member instanceof JavaWay;
    }

    public boolean isNode() {
        return member instanceof JavaNode;
    }

    public JavaRelation getRelation() {
        return (JavaRelation) member;
    }

    public JavaWay getWay() {
        return (JavaWay) member;
    }

    public JavaNode getNode() {
        return (JavaNode) member;
    }

    public JavaElement getMember() {
        return member;
    }

    public JavaMember(String role, JavaElement member) {
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
        JavaMember that = (JavaMember) obj;
        return Objects.equals(role, that.role) &&
                Objects.equals(member, that.member);
    }
}
