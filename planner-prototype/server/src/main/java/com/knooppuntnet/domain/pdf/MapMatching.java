package com.knooppuntnet.domain.pdf;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class MapMatching implements Serializable {

	@JsonProperty("hints")
	private Object hints;

	@JsonProperty("info")
	private Object info;

	@JsonProperty("paths")
	private Path path;

	@JsonProperty("mpa_matching")
	private Object map_matching;

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public Object getHints() {
		return hints;
	}

	public void setHints(Object hints) {
		this.hints = hints;
	}

	public Object getInfo() {
		return info;
	}

	public void setInfo(Object info) {
		this.info = info;
	}

	public Object getMap_matching() {
		return map_matching;
	}

	public void setMap_matching(Object map_matching) {
		this.map_matching = map_matching;
	}
}
