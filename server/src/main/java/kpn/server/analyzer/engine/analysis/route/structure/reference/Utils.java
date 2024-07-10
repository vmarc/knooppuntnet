package kpn.server.analyzer.engine.analysis.route.structure.reference;

import kpn.server.analyzer.engine.analysis.route.structure.reference.ReferenceLink.Direction;

import static kpn.server.analyzer.engine.analysis.route.structure.reference.ReferenceLink.Direction.NONE;
import static kpn.server.analyzer.engine.analysis.route.structure.reference.ReferenceLink.Direction.ROUNDABOUT_RIGHT;

public class Utils {

    static boolean isUnidirectional(final Member member) {
        return hasRoleForward(member) || hasRoleBackward(member);
    }

    static boolean hasRoleBackward(final Member member) {
        return "backward".equals(member.getRole());
    }

    static boolean hasRoleForward(final Member member) {
        return "forward".equals(member.getRole());
    }

    static Direction determineRoundabout(Member member) {
        if (member == null || !member.isWay()) return NONE;
        return determineRoundabout(member.getWay());
    }

    static Direction determineRoundabout(Way way) {
        if (way != null && way.hasTag("junction", "circular", "roundabout")) {
            int nodesCount = way.getNodesCount();
            if (nodesCount > 2 && way.getNode(0).getId() == way.getNode(way.getNodesCount() - 1).getId()) {
                return ROUNDABOUT_RIGHT;
            }
        }
        return NONE;
    }
}
