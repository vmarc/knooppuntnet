package kpn.planner.util;

public enum RoadType {
	cycling("bike"),
	hiking("foot");

	private String name;

	RoadType(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}
}
