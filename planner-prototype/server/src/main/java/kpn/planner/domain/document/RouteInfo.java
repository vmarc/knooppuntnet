package kpn.planner.domain.document;

import java.util.ArrayList;
import java.util.List;

public class RouteInfo {

	private float changeSetId;
	private boolean orphan;
	private String lastUpdated;
	private List<Object> tags;
	private List<Object> facts;
	private float version;
	private RouteInfoAnalysis analysis;
	private boolean ignored;
	private RouteSummary summary;
	private boolean display;
	private boolean active;

	public RouteInfo() {
	}

	public float getChangeSetId() {
		return changeSetId;
	}

	public void setChangeSetId(float changeSetId) {
		this.changeSetId = changeSetId;
	}

	public boolean getOrphan() {
		return orphan;
	}

	public void setOrphan(boolean orphan) {
		this.orphan = orphan;
	}

	public String getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(String lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public float getVersion() {
		return version;
	}

	public void setVersion(float version) {
		this.version = version;
	}

	public RouteInfoAnalysis getAnalysis() {
		return analysis;
	}

	public void setAnalysis(RouteInfoAnalysis analysis) {
		this.analysis = analysis;
	}

	public boolean getIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	public RouteSummary getSummary() {
		return summary;
	}

	public void setSummary(RouteSummary summary) {
		this.summary = summary;
	}

	public boolean getDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean getActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public List<Object> getTags() {
		return tags;
	}

	public void setTags(ArrayList<Object> tags) {
		this.tags = tags;
	}

	public List<Object> getFacts() {
		return facts;
	}

	public void setFacts(ArrayList<Object> facts) {
		this.facts = facts;
	}
}

