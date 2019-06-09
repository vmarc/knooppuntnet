package kpn.planner.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Section {

	private Long startNodeId;
	private Long endNodeId;
	private String startNode;
	private String endNode;
	private Long meters;
	private List<Coordinates> coordinates = new ArrayList<>();
	private Map<String, Coordinates> waypoints = new HashMap<>();

	public String getStartNode() {
		return startNode;
	}

	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}

	public String getEndNode() {
		return endNode;
	}

	public void setEndNode(String endNode) {
		this.endNode = endNode;
	}

	public Long getStartNodeId() {
		return startNodeId;
	}

	public void setStartNodeId(Long startNodeId) {
		this.startNodeId = startNodeId;
	}

	public Long getEndNodeId() {
		return endNodeId;
	}

	public void setEndNodeId(Long endNodeId) {
		this.endNodeId = endNodeId;
	}

	public Long getMeters() {
		return meters;
	}

	public void setMeters(Long meters) {
		this.meters = meters;
	}

	public List<Coordinates> getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(List<Coordinates> coordinates) {
		this.coordinates = coordinates;
	}

	public Map<String, Coordinates> getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(Map<String, Coordinates> waypoints) {
		this.waypoints = waypoints;
	}

	public void addWayPoint(String name, Coordinates coordinates) {
		this.waypoints.put(name, coordinates);
	}

	public void addAllCoordinates(List<Coordinates> coordinatesList) {
		this.coordinates.addAll(coordinatesList);
	}

	public void addCoordinates(Coordinates coordinates) {
		this.coordinates.add(coordinates);
	}
}
