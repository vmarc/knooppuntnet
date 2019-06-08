package kpn.planner.domain.document;

import java.util.List;

public class Analysis {

	private Map map;
	private String expectedName;
	private List<Node> startNodes;
	private List<Node> startTentacleNodes;
	private List<Object> structureStrings;
	private List<Member> members;
	private List<Node> endTentacleNodes;
	private List<Node> endNodes;
	private List<Object> unexpectedNodeIds;

	public Analysis() {
	}

	public Map getMap() {
		return map;
	}

	public void setMap(Map map) {
		this.map = map;
	}

	public String getExpectedName() {
		return expectedName;
	}

	public void setExpectedName(String expectedName) {
		this.expectedName = expectedName;
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

	public List<Object> getStructureStrings() {
		return structureStrings;
	}

	public void setStructureStrings(List<Object> structureStrings) {
		this.structureStrings = structureStrings;
	}

	public List<Member> getMembers() {
		return members;
	}

	public void setMembers(List<Member> members) {
		this.members = members;
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

	public List<Object> getUnexpectedNodeIds() {
		return unexpectedNodeIds;
	}

	public void setUnexpectedNodeIds(List<Object> unexpectedNodeIds) {
		this.unexpectedNodeIds = unexpectedNodeIds;
	}
}

