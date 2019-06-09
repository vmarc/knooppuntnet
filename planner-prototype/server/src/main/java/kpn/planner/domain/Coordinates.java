package kpn.planner.domain;

import java.util.Objects;

public class Coordinates {

	private String lat;
	private String lon;

	public Coordinates() {
	}

	public Coordinates(String lat, String lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		Coordinates that = (Coordinates) o;
		return Objects.equals(getLat(), that.getLat()) && Objects.equals(getLon(), that.getLon());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getLat(), getLon());
	}
}
