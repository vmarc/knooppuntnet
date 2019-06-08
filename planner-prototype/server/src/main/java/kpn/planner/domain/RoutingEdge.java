package kpn.planner.domain;

import org.jgrapht.graph.DefaultWeightedEdge;

/*
	Declaring own Edge class. Replaces DefaultEdge class.
	Additional attributes can be added if necessary.
 */
public class RoutingEdge extends DefaultWeightedEdge {

	private String routeId;
	private Long pathIndex;
	private String pathType;
	private Long meters;

	public RoutingEdge() {
	}

	public RoutingEdge(String routeId, Long pathIndex, String pathType, Long meters) {
		this.routeId = routeId;
		this.pathIndex = pathIndex;
		this.pathType = pathType;
		this.meters = meters;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public Long getPathIndex() {
		return pathIndex;
	}

	public void setPathIndex(Long pathIndex) {
		this.pathIndex = pathIndex;
	}

	public String getPathType() {
		return pathType;
	}

	public void setPathType(String pathType) {
		this.pathType = pathType;
	}

	public Long getMeters() {
		return meters;
	}

	public void setMeters(Long meters) {
		this.meters = meters;
	}
}
