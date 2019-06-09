package kpn.planner.domain.document;

import java.util.List;

public class TrackPath {

	private Long startNodeId;
	private Long endNodeId;
	private Long meters;
	private List<TrackSegment> segments;

	public TrackPath() {
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

	public List<TrackSegment> getSegments() {
		return segments;
	}

	public void setSegments(List<TrackSegment> segments) {
		this.segments = segments;
	}
}
