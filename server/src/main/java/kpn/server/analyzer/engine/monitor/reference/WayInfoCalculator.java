package kpn.server.analyzer.engine.monitor.reference;

import kpn.server.analyzer.engine.monitor.reference.WayInfo.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static kpn.server.analyzer.engine.monitor.reference.WayInfo.Direction.*;

public class WayInfoCalculator {

    private final Relation relation;
    private final List<Member> members;
    private final List<WayInfo> wayInfos;
    private static final int UNCONNECTED = Integer.MIN_VALUE;
    private int lastForwardWayMemberIndex;
    private int lastBackwardWayMemberIndex;
    private boolean onewayBeginning;
    private int firstGroupIdx;
    private int logIndent;

    public WayInfoCalculator(final Relation relation, final List<Member> members) {
        this.relation = relation;
        this.members = members;
        this.wayInfos = new ArrayList<>(Collections.nCopies(members.size(), null));
    }

    public List<WayInfo> calculate() {
        firstGroupIdx = 0;
        lastForwardWayMemberIndex = UNCONNECTED;
        lastBackwardWayMemberIndex = UNCONNECTED;
        onewayBeginning = false;
        WayInfo previousWayInfo = null;
        for (int memberIndex = 0; memberIndex < members.size(); memberIndex++) {
            previousWayInfo = calculateMember(previousWayInfo, memberIndex);
        }
        makeLoopIfNeeded(members.size() - 1);
        return wayInfos;
    }

    private WayInfo calculateMember(
            final WayInfo previousWayInfo,
            final int memberIndex
    ) {
        final Member member = members.get(memberIndex);
        String memberInfo = "";
        if (member.isWay()) {
            memberInfo = member.getWay().toString();
        }
        logFunctionStart("calculateMember(memberIndex=%d) role=\"%s\", %s", memberIndex, member.getRole(), memberInfo);
        try {
            WayInfo wayInfo = calculateNextWayInfo(previousWayInfo, memberIndex, member);
            if (!wayInfo.linkedToPreviousMember) {
                log("not linked to previous member");
                if (memberIndex > 0) {
                    makeLoopIfNeeded(memberIndex - 1);
                }
                firstGroupIdx = memberIndex;
            }
            return wayInfo;
        } finally {
            logFunctionEnd();
        }
    }

    private static boolean isNotWayMember(final Member m) {
        return !m.isWay() || m.getWay() == null;
    }

    private WayInfo calculateNextWayInfo(
            final WayInfo previousWayInfo,
            final int currentMemberIndex,
            final Member currentMember
    ) {
        logFunctionStart("calculateNextWayInfo(currentMemberIndex=%d)", currentMemberIndex);
        try {
            WayInfo wayInfo = new WayInfo(false);

            // MV the value of linkedToPreviousMember is not necessarily correct after following statement
            //      will be true if there is a previous member even if there is no connection with that member!
            wayInfo.linkedToPreviousMember = currentMemberIndex > 0 // MV not linked to previous member if current member is the first member
                    && wayInfos.get(currentMemberIndex - 1) != null
                    && wayInfos.get(currentMemberIndex - 1).isNodeOrRelationMember(); // MV not linked to previous member if previous is not way
            wayInfo.direction = NONE;

            if (Utils.isUnidirectional(currentMember)) {
                handleOneway(previousWayInfo, currentMemberIndex, wayInfo);
            }

            if (wayInfo.linkedToPreviousMember) {
                if (lastBackwardWayMemberIndex != UNCONNECTED && lastForwardWayMemberIndex != UNCONNECTED) {
                    determineOnewayWayInfo(currentMember, currentMemberIndex, wayInfo);
                    if (!wayInfo.linkedToPreviousMember) {
                        firstGroupIdx = currentMemberIndex;
                        log("set firstGroupIndex=%d", firstGroupIdx);
                    }
                }

                if (previousWayInfo != null && !Utils.isUnidirectional(currentMember)) {
                    wayInfo.direction = determineDirectionBasedOnReference(currentMemberIndex - 1, previousWayInfo.direction, currentMemberIndex);
                    // MV: here is linkedToPreviousMember corrected when it was wrongfully set to true before
                    wayInfo.linkedToPreviousMember = wayInfo.direction != NONE;
                }
            }

            if (!wayInfo.linkedToPreviousMember) {
                wayInfo.direction = determineDirectionOfFirst(currentMemberIndex, currentMember, false);
                log("set direction=%S", wayInfo.direction.toString());
                if (Utils.isUnidirectional(currentMember)) {
                    wayInfo.isOnewayLoopForwardPart = true;
                    lastForwardWayMemberIndex = currentMemberIndex;
                    log("set isOnewayLoopForwardPart=true");
                    log("set lastForwardWayMemberIndex=%d", lastForwardWayMemberIndex);
                }
            }

            wayInfo.linkedToNextMember = false;
            if (previousWayInfo != null) {
                previousWayInfo.linkedToNextMember = wayInfo.linkedToPreviousMember;
            }

            wayInfos.set(currentMemberIndex, wayInfo);
            return wayInfo;
        } finally {
            logFunctionEnd();
        }
    }

    private void handleOneway(final WayInfo previousWayInfo, final int memberIndex, final WayInfo wayInfo) {
        logFunctionStart("handleOneway(memberIndex=%d)", memberIndex);
        try {
            if (previousWayInfo != null && previousWayInfo.isOnewayTail) {
                wayInfo.isOnewayHead = true;
            }
            if (lastBackwardWayMemberIndex == UNCONNECTED && lastForwardWayMemberIndex == UNCONNECTED) {
                wayInfo.isOnewayHead = true;
                lastForwardWayMemberIndex = memberIndex - 1;
                lastBackwardWayMemberIndex = memberIndex - 1;
                onewayBeginning = true;

                log("beginning of new oneway");
                log("  set isOnewayHead=true");
                log("  set lastForwardWayMemberIndex=%d", lastForwardWayMemberIndex);
                log("  set lastBackwardWayMemberIndex=%d", lastBackwardWayMemberIndex);
                log("  set onewayBeginning=true");
            }
        } finally {
            logFunctionEnd();
        }
    }

    private void makeLoopIfNeeded(final int memberIndex) {
        logFunctionStart("makeLoopIfNeeded(memberIndex=%d) firstGroupIdx=%d", memberIndex, firstGroupIdx);
        try {
            boolean loop = false;
            if (memberIndex == firstGroupIdx) { //is primitive loop
                log("primitive loop");
                loop = determineDirectionBasedOnReference(memberIndex, FORWARD, memberIndex) == FORWARD;
            } else if (memberIndex >= 0) {
                loop = determineDirectionBasedOnReference(memberIndex, wayInfos.get(memberIndex).direction, firstGroupIdx) == wayInfos.get(firstGroupIdx).direction;
            }
            if (loop) {
                log("found loop");
                for (int j = firstGroupIdx; j <= memberIndex; ++j) {
                    wayInfos.get(j).isLoop = true;
                }
            } else {
                log("not a loop");
            }
        } finally {
            logFunctionEnd();
        }
    }

    private Direction determineDirectionOfFirst(final int memberIndex, final Member member, final boolean reversed) {
        logFunctionStart("determineDirectionOfFirst(memberIndex=%d, reversed=%s)", memberIndex, "" + reversed);
        try {
            Direction result = Utils.determineRoundabout(member);
            if (result == ROUNDABOUT_LEFT || result == ROUNDABOUT_RIGHT) {
                log("result member is roundabout %s)", result.toString());
                return result;
            }

            if (Utils.isUnidirectional(member)) {
                if (Utils.hasRoleBackward(member) != reversed) {
                    log("unidirectional with backward role (reversed=%s)", "" + reversed);
                    log("result direction BACKWARD");
                    return BACKWARD;
                } else {
                    log("unidirectional with forward role (reversed=%s)", "" + reversed);
                    log("result direction FORWARD");
                    return FORWARD;
                }
            } else {
                log("bidirectional way");
                log("look ahead to next member (memberIndex=%d) to determine direction", memberIndex + 1);
                logFunctionStart("assume direction FORWARD and see if it fits with the next member", memberIndex, "" + reversed);
                try {
                    // MV make the current member the 'previousMember' in the next call:
                    if (determineDirectionBasedOnReference(memberIndex, FORWARD, memberIndex + 1) != NONE) {
                        // MV the direction of the next member could be determined if we assume this member is FORWARD, so we make this one FORWARD
                        log("result direction FORWARD based on next member");
                        return FORWARD;
                    }
                } finally {
                    logFunctionEnd();
                }

                logFunctionStart("assume direction BACKWARD and see if it fits with the next member", memberIndex, "" + reversed);
                try {
                    if (determineDirectionBasedOnReference(memberIndex, BACKWARD, memberIndex + 1) != NONE) {
                        // MV the direction of the next member could be determined if we assume this member is BACKWARD, so we make this one BACKWARD
                        log("result direction BACKWARD based on next member");
                        return BACKWARD;
                    }
                } finally {
                    logFunctionEnd();
                }
            }
            log("result direction NONE");
            return NONE;
        } finally {
            logFunctionEnd();
        }
    }

    private void determineOnewayWayInfo(final Member member, final int memberIndex, final WayInfo wayInfo) {
        logFunctionStart("determineOnewayWayInfo(memberIndex=%d)", memberIndex);
        try {
            Direction dirFW = determineDirectionBasedOnReference(lastForwardWayMemberIndex, wayInfos.get(lastForwardWayMemberIndex).direction, memberIndex);
            log("dirFW=%s (with reference to lastForwardWayMemberIndex=%d)", dirFW.toString(), lastForwardWayMemberIndex);
            Direction dirBW;
            if (onewayBeginning) {
                log("onewayBeginning was true");
                if (lastBackwardWayMemberIndex < 0) {
                    log("lastBackwardWayMemberIndex not set");
                    dirBW = determineDirectionBasedOnReference(firstGroupIdx, reverse(wayInfos.get(firstGroupIdx).direction), memberIndex, true /* MV !!!*/);
                    log("dirBW=%s (with reference to firstGroupIdx=%d reversed)", dirFW.toString(), firstGroupIdx);
                } else {
                    dirBW = determineDirectionBasedOnReference(lastBackwardWayMemberIndex, wayInfos.get(lastBackwardWayMemberIndex).direction, memberIndex, true /* MV !!!*/);
                    log("dirBW=%s (with reference to lastBackwardWayMemberIndex=%d  reversed)", dirFW.toString(), lastBackwardWayMemberIndex);
                }

                // Support split-start routes. When the current way does
                // not fit as forward or backward and we have no backward
                // ways yet (onewayBeginning) and the most recent oneway
                // head starts a new segment (!linkPrev), instead of
                // disconnecting the current way, make it the start of the
                // backward route. To render properly, unset isOnewayHead on
                // the most recent head (since the current backward way does
                // no longer start there).
                if (dirFW == NONE && dirBW == NONE && Utils.isUnidirectional(member) && !wayInfo.isOnewayHead) {
                    WayInfo prevHead = null;
                    for (int j = memberIndex - 1; j >= 0; --j) { // go back to find the last onewayHead
                        if (wayInfos.get(j).isOnewayHead) {
                            prevHead = wayInfos.get(j);
                            break;
                        }
                    }

                    if (prevHead != null && !prevHead.linkedToPreviousMember) {
                        dirBW = determineDirectionOfFirst(memberIndex, member, true);
                        prevHead.isOnewayHead = false;
                    }
                }

                if (dirBW != NONE) {
                    onewayBeginning = false;
                }
            } else {
                dirBW = determineDirectionBasedOnReference(lastBackwardWayMemberIndex, wayInfos.get(lastBackwardWayMemberIndex).direction, memberIndex, true /* MV !!! */);
                log("dirBW=%s (with reference to lastForwardWayMemberIndex=%d)", dirFW.toString(), lastForwardWayMemberIndex);
            }

            if (Utils.isUnidirectional(member)) {
                log("unidirectional way");
                if (dirBW != NONE) {
                    wayInfo.direction = dirBW;
                    lastBackwardWayMemberIndex = memberIndex;
                    wayInfo.isOnewayLoopBackwardPart = true;
                    log("set direction=%s", wayInfo.direction.toString());
                    log("set lastBackwardWayMemberIndex=%d", lastBackwardWayMemberIndex);
                    log("set isOnewayLoopBackwardPart=true");
                }
                if (dirFW != NONE) {
                    wayInfo.direction = dirFW;
                    lastForwardWayMemberIndex = memberIndex;
                    wayInfo.isOnewayLoopForwardPart = true;
                    log("set direction=%s", wayInfo.direction.toString());
                    log("set lastForwardWayMemberIndex=%d", lastForwardWayMemberIndex);
                    log("set isOnewayLoopForwardPart=true");
                }
                // Not connected to previous
                if (dirFW == NONE && dirBW == NONE) {
                    wayInfo.linkedToPreviousMember = false;
                    wayInfo.isOnewayHead = true;
                    lastForwardWayMemberIndex = memberIndex - 1;
                    lastBackwardWayMemberIndex = memberIndex - 1;
                    onewayBeginning = true;
                    log("not connected to previous");
                    log("set isOnewayHead=true");
                    log("set lastForwardWayMemberIndex=%d", lastForwardWayMemberIndex);
                    log("set lastBackwardWayMemberIndex=%d", lastBackwardWayMemberIndex);
                    log("set onewayBeginning=true");
                }

                if (dirFW != NONE && dirBW != NONE) { //End of oneway loop
                    log("end of oneway loop");
                    if (memberIndex + 1 < members.size() && determineDirectionBasedOnReference(memberIndex, dirFW, memberIndex + 1) != NONE) {
                        wayInfo.isOnewayLoopBackwardPart = false;
                        wayInfo.direction = dirFW;
                        log("after looking at next member:");
                        log("  isOnewayLoopBackwardPart=false");
                        log("  direction=%s", wayInfo.direction.toString());
                    } else {
                        wayInfo.isOnewayLoopForwardPart = false;
                        wayInfo.direction = dirBW;
                        log("could not derive from next member:");
                        log("  isOnewayLoopForwardPart=false");
                        log("  direction=%s", wayInfo.direction.toString());
                    }

                    wayInfo.isOnewayTail = true;
                    log("set isOnewayTail=true");
                }

            } else {
                log("bidirectional way");
                lastForwardWayMemberIndex = UNCONNECTED;
                lastBackwardWayMemberIndex = UNCONNECTED;
                log("  set lastForwardWayMemberIndex=UNCONNECTED");
                log("  set lastBackwardWayMemberIndex=UNCONNECTED");
                if (dirFW == NONE || dirBW == NONE) {
                    wayInfo.linkedToPreviousMember = false;
                }
            }
        } finally {
            logFunctionEnd();
        }
    }

    private static Direction reverse(final Direction direction) {
        if (direction == FORWARD) return BACKWARD;
        if (direction == BACKWARD) return FORWARD;
        return direction;
    }

    private Direction determineDirectionBasedOnReference(final int referenceMemberIndex, final Direction referenceDirection, final int memberIndex) {
        return determineDirectionBasedOnReference(referenceMemberIndex, referenceDirection, memberIndex, false);
    }

    private Direction determineDirectionBasedOnReference(final int referenceMemberIndex, final Direction referenceDirection, final int memberIndex, final boolean reversed) {
        logFunctionStart("determineDirectionBasedOnReference(referenceMemberIndex=%d, referenceDirection=%s, memberIndex=%d, reversed=%s)", referenceMemberIndex, referenceDirection.toString(), memberIndex, "" + reversed);
        try {
            if (members == null || referenceMemberIndex < 0 // MV processing the first way
                    || memberIndex < 0 // MV invalid state
                    || referenceMemberIndex >= members.size() // MV invalid state
                    || memberIndex >= members.size() // MV invalid state
                    || referenceDirection == NONE  // MV cannot determine direction relative to unknown direction
            ) {
                log("result NONE (cannot determine direction relative to unknown direction)");
                return NONE;
            }

            final Member referenceMember = members.get(referenceMemberIndex);
            final Member currentMember = members.get(memberIndex);
            Way previousWay = null;
            Way currentWay = null;

            if (referenceMember.isWay()) {
                previousWay = referenceMember.getWay();
            }
            if (currentMember.isWay()) {
                currentWay = currentMember.getWay();
            }

            if (previousWay == null || currentWay == null) {
                log("result NONE (cannot determine direction if reference or current member is not way)");
                return NONE;
            }
            /* the list of nodes the way k can dock to */
            List<Node> previousWayConnectionCandiateNodes = new ArrayList<>();

            switch (referenceDirection) {
                case FORWARD:
                    previousWayConnectionCandiateNodes.add(previousWay.lastNode());
                    break;
                case BACKWARD:
                    previousWayConnectionCandiateNodes.add(previousWay.firstNode());
                    break;
                case ROUNDABOUT_LEFT:
                case ROUNDABOUT_RIGHT:
                    previousWayConnectionCandiateNodes = previousWay.getNodes();
                    break;
                default: // Do nothing
            }

            String nodeIdsString = previousWayConnectionCandiateNodes.stream().map(node -> node.getId().toString()).collect(joining(","));
            log("reference way candidateNodeIds: [%s]", nodeIdsString);

            for (Node previousWayNode : previousWayConnectionCandiateNodes) {
                if (Utils.determineRoundabout(members.get(memberIndex)) != NONE) {
                    log("current member is roundabout");
                    for (Node currentWayNode : currentWay.getNodes()) {
                        if (previousWayNode == currentWayNode) {
                            return Utils.determineRoundabout(members.get(memberIndex));
                        }
                    }
                } else if (Utils.isUnidirectional(currentMember)) {
                    log("current member is unidirectional");
                    if (!reversed && previousWayNode == onewayStartNode(currentMember)) {
                        log("previousWayNode %d matches oneway startnode", previousWayNode.getId());
                        if (Utils.hasRoleBackward(currentMember)) {
                            log("result BACKWARD because current member has role 'backward'");
                            return BACKWARD;
                        } else {
                            log("result FORWARD because current member has role 'forward'");
                            return FORWARD;
                        }
                    }
                    if (reversed && previousWayNode == onewayEndNode(currentMember)) {
                        log("previousWayNode %d matches oneway endnode", previousWayNode.getId());
                        if (Utils.hasRoleBackward(currentMember)) {
                            log("result FORWARD because current member has role 'backward'");
                            return FORWARD;
                        } else {
                            log("result BACKWARD because current member has role 'forward'");
                            return BACKWARD;
                        }
                    }
                } else {
                    log("current member is bidirectional");
                    if (previousWayNode == currentWay.firstNode()) {
                        log("result FORWARD because previousWayNode %d matches currentWay first node", previousWayNode.getId());
                        return FORWARD;
                    }
                    if (previousWayNode == currentWay.lastNode()) {
                        log("result BACKWARD because previousWayNode %d matches currentWay last node", previousWayNode.getId());
                        return BACKWARD;
                    }
                }
            }
            log("result NONE because no connection found between member %d and member %d", referenceMemberIndex, memberIndex);
            return NONE;
        } finally {
            logFunctionEnd();
        }
    }

    private static boolean isConnected(final Way way1, final Way way2) {
        return way1 != null && way2 != null && (way1.isFirstLastNode(way2.firstNode()) || way1.isFirstLastNode(way2.lastNode()));
    }

    public Node onewayStartNode(Member member) {
        if (!member.isWay()) {
            return null;
        }
        if ("backward".equals(member.getRole())) {
            return member.getWay().lastNode();
        }
        return member.getWay().firstNode();
    }

    public Node onewayEndNode(Member member) {
        if (!member.isWay()) {
            return null;
        }
        if ("backward".equals(member.getRole())) {
            return member.getWay().firstNode();
        }
        return member.getWay().lastNode();
    }

    private void logFunctionStart(final String format, final Object... args) {
        log(format, args);
        logIndent += 1;
    }

    private void logFunctionEnd() {
        logIndent -= 1;
    }

    private void log(final String format, final Object... args) {
        for (int i = 0; i < logIndent; i++) {
            System.out.print("  ");
        }
        System.out.printf(format, args);
        System.out.println();
    }
}
