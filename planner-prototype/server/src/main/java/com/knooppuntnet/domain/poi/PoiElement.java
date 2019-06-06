package com.knooppuntnet.domain.poi;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PoiElement {

	@JsonProperty("_id")
	private String _id;
	@JsonProperty("_rev")
	private String _rev;
	@JsonProperty("poi")
	private Poi poi;

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

	public Poi getPoi() {
		return poi;
	}

	public void setPoi(Poi poi) {
		this.poi = poi;
	}
}
