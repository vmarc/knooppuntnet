package kpn.server.analyzer.engine.monitor.reference;

import kpn.server.analyzer.engine.monitor.reference.WayInfo.Direction;

import static kpn.server.analyzer.engine.monitor.reference.WayInfo.Direction.NONE;
import static kpn.server.analyzer.engine.monitor.reference.WayInfo.Direction.ROUNDABOUT_RIGHT;

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
            if (nodesCount > 2) {
                return ROUNDABOUT_RIGHT;
            }
        }
        return NONE;
    }
}
