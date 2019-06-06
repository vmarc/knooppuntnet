package com.knooppuntnet.domain.document;

import java.util.List;

public class Path {

	private Long startNodeId;
	private Long endNodeId;
	private Long meters;
	private List<Segment> segments;

	public Path() {
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

	public List<Segment> getSegments() {
		return segments;
	}

	public void setSegments(List<Segment> segments) {
		this.segments = segments;
	}
}
