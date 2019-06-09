package kpn.planner.domain.document;

import kpn.planner.domain.Coordinates;

public class TrackPoint {

	private String lat;
	private String lon;

	public String getLat() {
		return lat;
	}

	public void setLat(final String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(final String lon) {
		this.lon = lon;
	}

	public Coordinates toCoordinates() {
		return new Coordinates(lat, lon);
	}
}
