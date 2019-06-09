package kpn.planner.domain.document;

public class TrackSegmentFragment {

	private TrackPoint trackPoint;
	private Integer meters;
	private Integer orientation;
	private Integer streetIndex;

	public TrackPoint getTrackPoint() {
		return trackPoint;
	}

	public void setTrackPoint(final TrackPoint trackPoint) {
		this.trackPoint = trackPoint;
	}

	public Integer getMeters() {
		return meters;
	}

	public void setMeters(final Integer meters) {
		this.meters = meters;
	}

	public Integer getOrientation() {
		return orientation;
	}

	public void setOrientation(final Integer orientation) {
		this.orientation = orientation;
	}

	public Integer getStreetIndex() {
		return streetIndex;
	}

	public void setStreetIndex(final Integer streetIndex) {
		this.streetIndex = streetIndex;
	}
}
