package kpn.planner.domain.document;

import java.util.ArrayList;
import java.util.List;

public class TrackSegment {

	private String surface;
	private TrackPoint source;
	private List<TrackSegmentFragment> fragments = new ArrayList<>();

	public String getSurface() {
		return surface;
	}

	public void setSurface(final String surface) {
		this.surface = surface;
	}

	public TrackPoint getSource() {
		return source;
	}

	public void setSource(final TrackPoint source) {
		this.source = source;
	}

	public List<TrackSegmentFragment> getFragments() {
		return fragments;
	}

	public void setFragments(final List<TrackSegmentFragment> fragments) {
		this.fragments = fragments;
	}
}
