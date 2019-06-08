package kpn.planner.domain.document;

import java.util.ArrayList;
import java.util.List;

public class Route {

	private float changeSetId;
	private boolean orphan;
	private String lastUpdated;
	private List<Object> tags;
	private List<Object> facts;
	private float version;
	private Analysis analysis;
	private boolean ignored;
	private Summary summary;
	private boolean display;
	private boolean active;

	public Route() {
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

	public Analysis getAnalysis() {
		return analysis;
	}

	public void setAnalysis(Analysis analysis) {
		this.analysis = analysis;
	}

	public boolean getIgnored() {
		return ignored;
	}

	public void setIgnored(boolean ignored) {
		this.ignored = ignored;
	}

	public Summary getSummary() {
		return summary;
	}

	public void setSummary(Summary summary) {
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

