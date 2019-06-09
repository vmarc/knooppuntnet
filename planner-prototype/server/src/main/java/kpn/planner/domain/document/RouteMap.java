package kpn.planner.domain.document;

import java.util.List;

public class RouteMap {

	private MapBounds bounds;
	private TrackPath forwardPath;
	private TrackPath backwardPath;
	private List<Object> unusedSegments;
	private List<TrackPath> startTentaclePaths;
	private List<TrackPath> endTentaclePaths;
	// forwardBreakPoint
	// backwardBreakPoint
	private List<RouteNetworkNodeInfo> startNodes;
	private List<RouteNetworkNodeInfo> endNodes;
	private List<RouteNetworkNodeInfo> startTentacleNodes;
	private List<RouteNetworkNodeInfo> endTentacleNodes;
	private List<Object> redundantNodes;
	private List<String> streets;

	public RouteMap() {
	}

	public TrackPath getForwardPath() {
		return forwardPath;
	}

	public void setForwardPath(TrackPath forwardPath) {
		this.forwardPath = forwardPath;
	}

	public List<TrackPath> getStartTentaclePaths() {
		return startTentaclePaths;
	}

	public void setStartTentaclePaths(List<TrackPath> startTentaclePaths) {
		this.startTentaclePaths = startTentaclePaths;
	}

	public MapBounds getBounds() {
		return bounds;
	}

	public void setBounds(MapBounds bounds) {
		this.bounds = bounds;
	}

	public List<Object> getUnusedSegments() {
		return unusedSegments;
	}

	public void setUnusedSegments(List<Object> unusedSegments) {
		this.unusedSegments = unusedSegments;
	}

	public List<RouteNetworkNodeInfo> getStartNodes() {
		return startNodes;
	}

	public void setStartNodes(List<RouteNetworkNodeInfo> startNodes) {
		this.startNodes = startNodes;
	}

	public List<RouteNetworkNodeInfo> getStartTentacleNodes() {
		return startTentacleNodes;
	}

	public void setStartTentacleNodes(List<RouteNetworkNodeInfo> startTentacleNodes) {
		this.startTentacleNodes = startTentacleNodes;
	}

	public List<TrackPath> getEndTentaclePaths() {
		return endTentaclePaths;
	}

	public void setEndTentaclePaths(List<TrackPath> endTentaclePaths) {
		this.endTentaclePaths = endTentaclePaths;
	}

	public List<Object> getRedundantNodes() {
		return redundantNodes;
	}

	public void setRedundantNodes(List<Object> redundantNodes) {
		this.redundantNodes = redundantNodes;
	}

	public TrackPath getBackwardPath() {
		return backwardPath;
	}

	public void setBackwardPath(TrackPath backwardPath) {
		this.backwardPath = backwardPath;
	}

	public List<RouteNetworkNodeInfo> getEndTentacleNodes() {
		return endTentacleNodes;
	}

	public void setEndTentacleNodes(List<RouteNetworkNodeInfo> endTentacleNodes) {
		this.endTentacleNodes = endTentacleNodes;
	}

	public List<RouteNetworkNodeInfo> getEndNodes() {
		return endNodes;
	}

	public void setEndNodes(List<RouteNetworkNodeInfo> endNodes) {
		this.endNodes = endNodes;
	}

	public List<String> getStreets() {
		return streets;
	}

	public void setStreets(final List<String> streets) {
		this.streets = streets;
	}
}

