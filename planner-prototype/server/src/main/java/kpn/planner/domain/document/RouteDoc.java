package kpn.planner.domain.document;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RouteDoc {

	@JsonProperty("_id")
	private String _id;

	@JsonProperty("_rev")
	private String _rev;

	@JsonProperty("route")
	private RouteInfo route;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_rev() {
		return _rev;
	}

	public void set_rev(String _rev) {
		this._rev = _rev;
	}

	public RouteInfo getRoute() {
		return route;
	}

	public void setRoute(RouteInfo routeObject) {
		this.route = routeObject;
	}
}

