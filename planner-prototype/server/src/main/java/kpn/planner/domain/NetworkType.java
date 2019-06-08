package kpn.planner.domain;

public enum NetworkType {
	cycling("rcn"),
	hiking("rwn"),
	horse("rhn"),
	motorboat("rmn"),
	canoe("rpn"),
	inlineSkating("rin");

	private final String tagKey;

	NetworkType(String tagKey) {
		this.tagKey = tagKey;
	}

	public String getTagKey() {
		return tagKey;
	}
}
