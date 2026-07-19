package kpn.server.analyzer.engine.analysis.route.structure.reference;

// equivalent of Josm WayConnectionType
public class JavaLink {

    public final Long memberIndex;
    private final boolean nodeOrRelationMember; // member does not contain a way
    public boolean linkedToPreviousMember;
    public boolean linkedToNextMember;
    public Direction direction;

    public enum Direction {
        FORWARD, // the first node of this way is connected to the previous way and/or the last node of this way is connected to the next way
        BACKWARD,
        ROUNDABOUT_LEFT, // tagged as roundabout and connected to the previous/next member
        ROUNDABOUT_RIGHT,
        NONE; // no connection to the previous or next member

        public boolean isRoundabout() {
            return this == ROUNDABOUT_RIGHT || this == ROUNDABOUT_LEFT;
        }
    }

    public boolean isLoop; // part of a closed loop of ways
    public boolean isOnewayLoopForwardPart;
    public boolean isOnewayLoopBackwardPart;
    public boolean isOnewayHead;
    public boolean isOnewayTail;

    public JavaLink(final Long memberIndex, final boolean nodeOrRelationMember) {
        this.memberIndex = memberIndex;
        this.linkedToPreviousMember = false;
        this.linkedToNextMember = false;
        this.isLoop = false;
        this.direction = Direction.NONE;
        this.nodeOrRelationMember = nodeOrRelationMember;
    }

    public boolean isNodeOrRelationMember() {
        return !nodeOrRelationMember;
    }
}
