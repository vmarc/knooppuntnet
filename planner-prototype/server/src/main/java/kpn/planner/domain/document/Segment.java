package kpn.planner.domain.document;

import java.util.ArrayList;
import java.util.List;

import kpn.planner.domain.Coordinates;

public class Segment {

	private String surface;
	private List<Coordinates> trackPoints = new ArrayList<>();

	public Segment() {
	}

	public String getSurface() {
		return surface;
	}

	public void setSurface(String surface) {
		this.surface = surface;
	}

	public List<Coordinates> getTrackPoints() {
		return trackPoints;
	}

	public void setTrackPoints(List<Coordinates> trackPoints) {
		this.trackPoints = trackPoints;
	}
}
