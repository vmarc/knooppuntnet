package kpn.server.analyzer.engine.analysis.route.structure.reference;

import kpn.server.analyzer.engine.analysis.route.structure.reference.JavaLink.Direction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.joining;
import static kpn.server.analyzer.engine.analysis.route.structure.reference.JavaLink.Direction.*;

// equivalent of Josm WayConnectionTypeCalculator
public class JavaLinkAnalyzer {

    private final boolean traceEnabled;
    private final List<JavaMember> members;
    private final List<JavaLink> links;
    private static final int UNCONNECTED = Integer.MIN_VALUE;
    private int lastForwardWayMemberIndex;
    private int lastBackwardWayMemberIndex;
    private boolean onewayBeginning;
    private int firstGroupIdx;
    private int logIndent;

    public static List<JavaLink> analyze(final List<JavaMember> members, final boolean traceEnabled) {
        return new JavaLinkAnalyzer(members, traceEnabled).analyze();
    }

    public JavaLinkAnalyzer(final List<JavaMember> members, final boolean traceEnabled) {
        this.traceEnabled = traceEnabled;
        this.members = members;
        this.links = new ArrayList<>(Collections.nCopies(members.size(), null));
    }

    public List<JavaLink> analyze() {
        firstGroupIdx = 0;
        lastForwardWayMemberIndex = UNCONNECTED;
        lastBackwardWayMemberIndex = UNCONNECTED;
        onewayBeginning = false;
        JavaLink previousLink = null;
        for (int memberIndex = 0; memberIndex < members.size(); memberIndex++) {
            previousLink = processMember(previousLink, memberIndex);
        }
        makeLoopIfNeeded(members.size() - 1);
        return links;
    }

    private JavaLink processMember(
            final JavaLink previousLink,
            final int memberIndex
    ) {
        final JavaMember member = members.get(memberIndex);
        String memberInfo = "";
        if (member.isWay()) {
            memberInfo = member.getWay().toString();
        }
        logFunctionStart("processMember(memberIndex=%d) role=\"%s\", %s", memberIndex, member.getRole(), memberInfo);
        try {
            final JavaLink link = processNextMember(previousLink, memberIndex, member);
            if (!link.linkedToPreviousMember) {
                log("not linked to previous member");
                if (memberIndex > 0) {
                    makeLoopIfNeeded(memberIndex - 1);
                }
                firstGroupIdx = memberIndex;
            }
            return link;
        } finally {
            logFunctionEnd();
            log("---");
        }
    }

    private static boolean isNotWayMember(final JavaMember m) {
        return !m.isWay() || m.getWay() == null;
    }

    private JavaLink processNextMember(
            final JavaLink previousLink,
            final int currentMemberIndex,
            final JavaMember currentMember
    ) {
        logFunctionStart("processNextMember(currentMemberIndex=%d)", currentMemberIndex);
        try {
            final JavaLink link = new JavaLink(false);

            // MV the value of linkedToPreviousMember is not necessarily correct after following statement
            //      will be true if there is a previous member even if there is no connection with that member!
            link.linkedToPreviousMember = currentMemberIndex > 0 // MV not linked to previous member if current member is the first member
                    && links.get(currentMemberIndex - 1) != null
                    && links.get(currentMemberIndex - 1).isNodeOrRelationMember(); // MV not linked to previous member if previous is not way
            link.direction = NONE;

            if (Utils.isUnidirectional(currentMember)) {
                handleOneway(previousLink, currentMemberIndex, link);
            }

            if (link.linkedToPreviousMember) {
                if (lastBackwardWayMemberIndex != UNCONNECTED && lastForwardWayMemberIndex != UNCONNECTED) {
                    processOnewayMember(currentMember, currentMemberIndex, link);
                    if (!link.linkedToPreviousMember) {
                        firstGroupIdx = currentMemberIndex;
                        log("set firstGroupIndex=%d", firstGroupIdx);
                    }
                }

                if (previousLink != null && !Utils.isUnidirectional(currentMember)) {
                    link.direction = determineDirectionBasedOnReference(currentMemberIndex - 1, currentMemberIndex, previousLink.direction);
                    // MV: here is linkedToPreviousMember corrected when it was wrongfully set to true before
                    link.linkedToPreviousMember = link.direction != NONE;
                }
            }

            if (!link.linkedToPreviousMember) {
                link.direction = determineFirstDirection(currentMemberIndex, currentMember, false);
                log("set direction=%s", link.direction);
                if (Utils.isUnidirectional(currentMember)) {
                    link.isOnewayLoopForwardPart = true;
                    lastForwardWayMemberIndex = currentMemberIndex;
                    log("set isOnewayLoopForwardPart=true");
                    log("set lastForwardWayMemberIndex=%d", lastForwardWayMemberIndex);
                }
            }

            link.linkedToNextMember = false;
            if (previousLink != null) {
                previousLink.linkedToNextMember = link.linkedToPreviousMember;
            }

            links.set(currentMemberIndex, link);
            return link;
        } finally {
            logFunctionEnd();
        }
    }

    private void handleOneway(final JavaLink previousLink, final int memberIndex, final JavaLink link) {
        logFunctionStart("handleOneway(memberIndex=%d)", memberIndex);
        try {
            if (previousLink != null && previousLink.isOnewayTail) {
                link.isOnewayHead = true;
            }
            if (lastBackwardWayMemberIndex == UNCONNECTED && lastForwardWayMemberIndex == UNCONNECTED) {
                link.isOnewayHead = true;
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
                loop = determineDirectionBasedOnReference(memberIndex, memberIndex, FORWARD) == FORWARD;
            } else if (memberIndex >= 0) {
                loop = determineDirectionBasedOnReference(memberIndex, firstGroupIdx, links.get(memberIndex).direction) == links.get(firstGroupIdx).direction;
            }
            if (loop) {
                log("found loop");
                for (int j = firstGroupIdx; j <= memberIndex; ++j) {
                    links.get(j).isLoop = true;
                }
            } else {
                log("not a loop");
            }
        } finally {
            logFunctionEnd();
        }
    }

    private Direction determineFirstDirection(final int memberIndex, final JavaMember member, final boolean reversed) {
        logFunctionStart("determineFirstDirection(memberIndex=%d, reversed=%s) there is no previous member to determine direction from", memberIndex, "" + reversed);
        try {
            Direction roundaboutDirection = Utils.determineRoundabout(member);
            if (roundaboutDirection == ROUNDABOUT_LEFT || roundaboutDirection == ROUNDABOUT_RIGHT) {
                log("result: direction=%s)", roundaboutDirection);
                return roundaboutDirection;
            }

            if (Utils.isUnidirectional(member)) {
                if (Utils.hasRoleBackward(member) != reversed) {
                    log("result: direction=BACKWARD, oneway with backward role (reversed=%s)", "" + reversed);
                    return BACKWARD;
                } else {
                    log("result: direction FORWARD, unidirectional with forward role (reversed=%s)", "" + reversed);
                    return FORWARD;
                }
            } else {
                logFunctionStart("bidirectional way, assume direction FORWARD and see if it fits with the next member", memberIndex, "" + reversed);
                try {
                    if (determineDirectionBasedOnReference(memberIndex, memberIndex + 1, FORWARD) != NONE) {
                        log("result: direction=FORWARD, we could make a connection to the next member");
                        return FORWARD;
                    }
                } finally {
                    logFunctionEnd();
                }

                logFunctionStart("bidirectional way, assume direction BACKWARD and see if it fits with the next member", memberIndex, "" + reversed);
                try {
                    if (determineDirectionBasedOnReference(memberIndex, memberIndex + 1, BACKWARD) != NONE) {
                        log("result: direction=BACKWARD, we could make a connection to the next member");
                        return BACKWARD;
                    }
                } finally {
                    logFunctionEnd();
                }
            }
            log("result: direction=NONE");
            return NONE;
        } finally {
            logFunctionEnd();
        }
    }

    private void processOnewayMember(final JavaMember member, final int memberIndex, final JavaLink link) {
        logFunctionStart("processOnewayMember(memberIndex=%d)", memberIndex);
        try {
            Direction dirFW;
            final Direction referenceDirectionA = links.get(lastForwardWayMemberIndex).direction;
            logFunctionStart("try direction from lastForwardWayMemberIndex=%d (direction=%s)", lastForwardWayMemberIndex, referenceDirectionA);
            try {
                dirFW = determineDirectionBasedOnReference(
                        lastForwardWayMemberIndex,
                        memberIndex,
                        referenceDirectionA
                );
            } finally {
                logFunctionEnd();
            }
            Direction dirBW;
            if (onewayBeginning) {
                log("onewayBeginning was true");
                if (lastBackwardWayMemberIndex < 0) {
                    final Direction referenceDirection = reverse(links.get(firstGroupIdx).direction);
                    logFunctionStart("there was no previous backward way yet, try reference based on reverse direction of member at firstGroupIndex=%d (direction=%s)", firstGroupIdx, referenceDirection);
                    try {
                        dirBW = determineDirectionBasedOnReference(
                                firstGroupIdx,
                                memberIndex,
                                referenceDirection,
                                true /* MV !!!*/
                        );
                    } finally {
                        logFunctionEnd();
                    }
                } else {
                    final Direction referenceDirection = links.get(lastBackwardWayMemberIndex).direction;
                    logFunctionStart("try reference based on direction of last backward member (lastBackwardWayMemberIndex=%d) (direction=%s)", lastBackwardWayMemberIndex, referenceDirection);
                    try {
                        dirBW = determineDirectionBasedOnReference(
                                lastBackwardWayMemberIndex,
                                memberIndex,
                                referenceDirection,
                                true /* MV !!!*/
                        );
                    } finally {
                        logFunctionEnd();
                    }
                }

                // Support split-start routes. When the current way does
                // not fit as forward or backward and we have no backward
                // ways yet (onewayBeginning) and the most recent oneway
                // head starts a new segment (!linkPrev), instead of
                // disconnecting the current way, make it the start of the
                // backward route. To render properly, unset isOnewayHead on
                // the most recent head (since the current backward way does
                // no longer start there).
                if (dirFW == NONE && dirBW == NONE && Utils.isUnidirectional(member) && !link.isOnewayHead) {
                    JavaLink prevHead = null;
                    for (int j = memberIndex - 1; j >= 0; --j) { // go back to find the last onewayHead
                        if (links.get(j).isOnewayHead) {
                            prevHead = links.get(j);
                            break;
                        }
                    }

                    if (prevHead != null && !prevHead.linkedToPreviousMember) {
                        dirBW = determineFirstDirection(memberIndex, member, true);
                        prevHead.isOnewayHead = false;
                    }
                }

                if (dirBW != NONE) {
                    onewayBeginning = false;
                }
            } else {
                final Direction referenceDirection = links.get(lastBackwardWayMemberIndex).direction;
                logFunctionStart("try reference based on direction of last backward member (lastBackwardWayMemberIndex=%d) (direction=%s)", lastBackwardWayMemberIndex, referenceDirection);
                try {
                    dirBW = determineDirectionBasedOnReference(
                            lastBackwardWayMemberIndex,
                            memberIndex,
                            referenceDirection,
                            true /* MV !!! */
                    );
                } finally {
                    logFunctionEnd();
                }
            }

            if (Utils.isUnidirectional(member)) {
                log("unidirectional way");
                if (dirBW != NONE) {
                    log("update link based on dirBW");
                    link.direction = dirBW;
                    lastBackwardWayMemberIndex = memberIndex;
                    link.isOnewayLoopBackwardPart = true;
                    log("  set direction=%s", link.direction);
                    log("  set lastBackwardWayMemberIndex=%d", lastBackwardWayMemberIndex);
                    log("  set isOnewayLoopBackwardPart=true");
                }
                if (dirFW != NONE) {
                    log("update link based on dirFW");
                    link.direction = dirFW;
                    lastForwardWayMemberIndex = memberIndex;
                    link.isOnewayLoopForwardPart = true;
                    log("  set direction=%s", link.direction);
                    log("  set lastForwardWayMemberIndex=%d", lastForwardWayMemberIndex);
                    log("  set isOnewayLoopForwardPart=true");
                }
                // Not connected to previous
                if (dirFW == NONE && dirBW == NONE) {
                    link.linkedToPreviousMember = false;
                    link.isOnewayHead = true;
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
                    if (memberIndex + 1 < members.size()) {
                        Direction dir;
                        logFunctionStart("try connecting current member (%d) to next member (%d) based on previously determined dirFW=%s", memberIndex, memberIndex + 1, dirFW);
                        try {
                            dir = determineDirectionBasedOnReference(memberIndex, memberIndex + 1, dirFW);
                        } finally {
                            logFunctionEnd();
                        }

                        if (dir != NONE) {
                            link.isOnewayLoopBackwardPart = false;
                            link.direction = dirFW;
                            log("found connection to next member:");
                            log("  set isOnewayLoopBackwardPart=false");
                            log("  set direction=%s", link.direction);
                        } else {
                            link.isOnewayLoopForwardPart = false;
                            link.direction = dirBW;
                            log("could not find connection to next member:");
                            log("  set isOnewayLoopForwardPart=false");
                            log("  set direction=%s", link.direction);
                        }
                    } else {
                        log("end of oneway loop, end of route (no more next members)");
                        link.isOnewayLoopForwardPart = false;
                        link.direction = dirBW;
                        log("could not derive from next member:");
                        log("  set isOnewayLoopForwardPart=false");
                        log("  set direction=%s", link.direction);
                    }

                    link.isOnewayTail = true;
                    log("set isOnewayTail=true");
                }

            } else {
                log("bidirectional way");
                lastForwardWayMemberIndex = UNCONNECTED;
                lastBackwardWayMemberIndex = UNCONNECTED;
                log("  set lastForwardWayMemberIndex=UNCONNECTED");
                log("  set lastBackwardWayMemberIndex=UNCONNECTED");
                if (dirFW == NONE || dirBW == NONE) {
                    link.linkedToPreviousMember = false;
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

    private Direction determineDirectionBasedOnReference(final int memberIndex1, final int memberIndex2, final Direction referenceDirection) {
        return determineDirectionBasedOnReference(memberIndex1, memberIndex2, referenceDirection, false);
    }

    private Direction determineDirectionBasedOnReference(final int memberIndex1, final int memberIndex2, final Direction referenceDirection, final boolean reversed) {
        logFunctionStart("determineDirectionBasedOnReference(memberIndex1=%d, memberIndex2=%d, referenceDirection=%s, reversed=%s)", memberIndex1, memberIndex2, referenceDirection, reversed);
        try {
            if (memberIndex1 < 0 || memberIndex1 >= members.size()) {
                log("result: direction=NONE (invalid state: no member1 with index %d)", memberIndex1);
                return NONE;
            }
            if (memberIndex2 < 0 || memberIndex2 >= members.size()) {
                log("result: direction=NONE (invalid state: no member with index %d)", memberIndex2);
                return NONE;
            }
            if (referenceDirection == NONE) {
                log("result: direction=NONE (invalid state: cannot determine direction relative to unknown direction)");
                return NONE;
            }

            final JavaMember member1 = members.get(memberIndex1);
            final JavaMember member2 = members.get(memberIndex2);
            JavaWay way1 = null;
            JavaWay way2 = null;

            if (member1.isWay()) {
                way1 = member1.getWay();
            }
            if (member2.isWay()) {
                way2 = member2.getWay();
            }

            if (way1 == null || way2 == null) {
                log("result: direction=NONE (cannot determine direction if reference or current member is not way)");
                return NONE;
            }

            final List<JavaNode> connectionCandidateNodes1 = new ArrayList<>();
            switch (referenceDirection) {
                case FORWARD:
                    connectionCandidateNodes1.add(way1.lastNode());
                    break;
                case BACKWARD:
                    connectionCandidateNodes1.add(way1.firstNode());
                    break;
                case ROUNDABOUT_LEFT:
                case ROUNDABOUT_RIGHT:
                    connectionCandidateNodes1.addAll(way1.getNodes());
                    break;
                default: // Do nothing
            }

            final String nodeIdsString = connectionCandidateNodes1.stream().map(node -> node.getId().toString()).collect(joining(","));
            log("connectionCandidateNodes1: [%s]", nodeIdsString);

            for (JavaNode node1 : connectionCandidateNodes1) {
                if (Utils.determineRoundabout(members.get(memberIndex2)) != NONE) {
                    for (JavaNode node2 : way2.getNodes()) {
                        if (node1 == node2) {
                            Direction dir = Utils.determineRoundabout(members.get(memberIndex2));
                            log("result: direction=%s, connecting node %d", dir, node2.getId());
                            return dir;
                        }
                    }
                } else if (Utils.isUnidirectional(member2)) {
                    if (!reversed && node1 == onewayStartNode(member2)) {
                        if (Utils.hasRoleBackward(member2)) {
                            log("result: direction=BACKWARD, connecting node %d (oneway start node, backward role)", node1.getId());
                            return BACKWARD;
                        } else {
                            log("result: direction=FORWARD, connecting node %d (oneway start node, no backward role)", node1.getId());
                            return FORWARD;
                        }
                    }
                    if (reversed && node1 == onewayEndNode(member2)) {
                        if (Utils.hasRoleBackward(member2)) {
                            log("result: direction=FORWARD, connecting node %d (oneway end node, backward role)", node1.getId());
                            return FORWARD;
                        } else {
                            log("result: direction=BACKWARD, connecting node %d (oneway end node, no backward role)", node1.getId());
                            return BACKWARD;
                        }
                    }
                } else {
                    if (node1 == way2.firstNode()) {
                        log("result: direction=FORWARD, connecting node %d (first node of bidirectional)", node1.getId());
                        return FORWARD;
                    }
                    if (node1 == way2.lastNode()) {
                        log("result: direction=FORWARD, connecting node %d (last node of bidirectional)", node1.getId());
                        return BACKWARD;
                    }
                }
            }
            log("result: direction=NONE, no connecting node");
            return NONE;
        } finally {
            logFunctionEnd();
        }
    }

    private static boolean isConnected(final JavaWay way1, final JavaWay way2) {
        return way1 != null && way2 != null && (way1.isFirstLastNode(way2.firstNode()) || way1.isFirstLastNode(way2.lastNode()));
    }

    public JavaNode onewayStartNode(JavaMember member) {
        if (!member.isWay()) {
            return null;
        }
        if ("backward".equals(member.getRole())) {
            return member.getWay().lastNode();
        }
        return member.getWay().firstNode();
    }

    public JavaNode onewayEndNode(JavaMember member) {
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
        if (traceEnabled) {
            for (int i = 0; i < logIndent; i++) {
                System.out.print("  ");
            }
            System.out.printf(format, args);
            System.out.println();
        }
    }
}
