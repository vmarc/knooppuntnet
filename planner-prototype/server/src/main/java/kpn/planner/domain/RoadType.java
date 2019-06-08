package kpn.planner.domain;

public enum RoadType {
	cycling("bike"),
	hiking("foot");

	private final String name;

	RoadType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
