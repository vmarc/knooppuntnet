package kpn.planner.domain.poi;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import kpn.planner.serializer.PoiDeserializer;

@JsonDeserialize(using = PoiDeserializer.class)
public class Poi {

	private String layer;
	private Double latitude;
	private Double longitude;
	private List<TagInformation> tags = new ArrayList<>();
	private Long elementId;
	private String elementType;

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public String getLayer() {
		return layer;
	}

	public void setLayer(String layers) {
		this.layer = layers;
	}

	public List<TagInformation> getTags() {
		return tags;
	}

	public void setTags(List<TagInformation> tags) {
		this.tags = tags;
	}

	public Long getElementId() {
		return elementId;
	}

	public void setElementId(Long elementId) {
		this.elementId = elementId;
	}

	public String getElementType() {
		return elementType;
	}

	public void setElementType(String elementType) {
		this.elementType = elementType;
	}

	public void addTagInformation(TagInformation tagInformation) {
		this.tags.add(tagInformation);
	}
}
