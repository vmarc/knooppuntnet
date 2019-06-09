package kpn.planner.domain.document;

import java.util.ArrayList;
import java.util.List;

public class RouteSummary {

	private String name;
	private String networkType;
	private String timestamp;
	private List<Object> tags;
	private String country;
	private float id;
	private float wayCount;
	private boolean isBroken;
	private float meters;
	private List<Object> nodeNames;

	public RouteSummary() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public float getId() {
		return id;
	}

	public void setId(float id) {
		this.id = id;
	}

	public float getWayCount() {
		return wayCount;
	}

	public void setWayCount(float wayCount) {
		this.wayCount = wayCount;
	}

	public boolean getIsBroken() {
		return isBroken;
	}

	public void setIsBroken(boolean isBroken) {
		this.isBroken = isBroken;
	}

	public float getMeters() {
		return meters;
	}

	public void setMeters(float meters) {
		this.meters = meters;
	}

	public List<Object> getTags() {
		return tags;
	}

	public void setTags(ArrayList<Object> tags) {
		this.tags = tags;
	}

	public List<Object> getNodeNames() {
		return nodeNames;
	}

	public void setNodeNames(ArrayList<Object> nodeNames) {
		this.nodeNames = nodeNames;
	}
}

