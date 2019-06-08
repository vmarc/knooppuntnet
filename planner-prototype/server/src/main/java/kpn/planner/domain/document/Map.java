package kpn.planner.domain.document;

import java.util.List;

public class Map {

	private Path forwardPath;
	private List<Path> startTentaclePaths;
	private Bounds bounds;
	private List<Object> unusedSegments;
	private List<Node> startNodes;
	private List<Node> startTentacleNodes;
	private List<Path> endTentaclePaths;
	private List<Object> redundantNodes;
	private Path backwardPath;
	private List<Node> endTentacleNodes;
	private List<Node> endNodes;

	public Map() {
	}

	public Path getForwardPath() {
		return forwardPath;
	}

	public void setForwardPath(Path forwardPath) {
		this.forwardPath = forwardPath;
	}

	public List<Path> getStartTentaclePaths() {
		return startTentaclePaths;
	}

	public void setStartTentaclePaths(List<Path> startTentaclePaths) {
		this.startTentaclePaths = startTentaclePaths;
	}

	public Bounds getBounds() {
		return bounds;
	}

	public void setBounds(Bounds bounds) {
		this.bounds = bounds;
	}

	public List<Object> getUnusedSegments() {
		return unusedSegments;
	}

	public void setUnusedSegments(List<Object> unusedSegments) {
		this.unusedSegments = unusedSegments;
	}

	public List<Node> getStartNodes() {
		return startNodes;
	}

	public void setStartNodes(List<Node> startNodes) {
		this.startNodes = startNodes;
	}

	public List<Node> getStartTentacleNodes() {
		return startTentacleNodes;
	}

	public void setStartTentacleNodes(List<Node> startTentacleNodes) {
		this.startTentacleNodes = startTentacleNodes;
	}

	public List<Path> getEndTentaclePaths() {
		return endTentaclePaths;
	}

	public void setEndTentaclePaths(List<Path> endTentaclePaths) {
		this.endTentaclePaths = endTentaclePaths;
	}

	public List<Object> getRedundantNodes() {
		return redundantNodes;
	}

	public void setRedundantNodes(List<Object> redundantNodes) {
		this.redundantNodes = redundantNodes;
	}

	public Path getBackwardPath() {
		return backwardPath;
	}

	public void setBackwardPath(Path backwardPath) {
		this.backwardPath = backwardPath;
	}

	public List<Node> getEndTentacleNodes() {
		return endTentacleNodes;
	}

	public void setEndTentacleNodes(List<Node> endTentacleNodes) {
		this.endTentacleNodes = endTentacleNodes;
	}

	public List<Node> getEndNodes() {
		return endNodes;
	}

	public void setEndNodes(List<Node> endNodes) {
		this.endNodes = endNodes;
	}
}

