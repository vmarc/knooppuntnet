package kpn.planner.domain.document;

import java.util.List;

public class RouteInfoAnalysis {

	private List<RouteNetworkNodeInfo> startNodes;
	private List<RouteNetworkNodeInfo> endNodes;
	private List<RouteNetworkNodeInfo> startTentacleNodes;
	private List<RouteNetworkNodeInfo> endTentacleNodes;
	private List<Long> unexpectedNodeIds;
	private List<RouteMemberInfo> members;
	private String expectedName;
	private RouteMap map;
	private List<String> structureStrings;

	public RouteInfoAnalysis() {
	}

	public RouteMap getMap() {
		return map;
	}

	public void setMap(RouteMap map) {
		this.map = map;
	}

	public String getExpectedName() {
		return expectedName;
	}

	public void setExpectedName(String expectedName) {
		this.expectedName = expectedName;
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

	public List<String> getStructureStrings() {
		return structureStrings;
	}

	public void setStructureStrings(List<String> structureStrings) {
		this.structureStrings = structureStrings;
	}

	public List<RouteMemberInfo> getMembers() {
		return members;
	}

	public void setMembers(List<RouteMemberInfo> members) {
		this.members = members;
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

	public List<Long> getUnexpectedNodeIds() {
		return unexpectedNodeIds;
	}

	public void setUnexpectedNodeIds(List<Long> unexpectedNodeIds) {
		this.unexpectedNodeIds = unexpectedNodeIds;
	}
}

