package kpn.server.analyzer.engine.monitor.reference;

public class WayInfo {

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

    public WayInfo(
            final boolean linkedToPreviousMember,
            final boolean linkedToNextMember,
            final Direction direction
    ) {
        this.linkedToPreviousMember = linkedToPreviousMember;
        this.linkedToNextMember = linkedToNextMember;
        this.isLoop = false;
        this.direction = direction;
        nodeOrRelationMember = false;
    }

    public WayInfo(final boolean nodeOrRelationMember) {
        this.linkedToPreviousMember = false;
        this.linkedToNextMember = false;
        this.isLoop = false;
        this.direction = Direction.NONE;
        this.nodeOrRelationMember = nodeOrRelationMember;
    }

    public WayInfo() {
        this(true);
    }

    public boolean isNodeOrRelationMember() {
        return !nodeOrRelationMember;
    }
}
