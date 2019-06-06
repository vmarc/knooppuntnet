package com.knooppuntnet.domain.pdf;

import java.util.ArrayList;
import java.util.List;

public class PdfDocument {

	private long totalMeters;
	private long totalTimeInSeconds;
	private long totalHoursParsed;
	private long totalMinutesParsed;
	private long totalSecondsParsed;
	private List<SectionInformation> informations = new ArrayList<>();

	public List<SectionInformation> getInformations() {
		return informations;
	}

	public void setInformations(List<SectionInformation> informations) {
		this.informations = informations;
	}

	public void addSectionInformation(SectionInformation sectionInformation) {
		this.informations.add(sectionInformation);
	}

	public long getTotalMeters() {
		return totalMeters;
	}

	public void setTotalMeters(long totalMeters) {
		this.totalMeters = totalMeters;
	}

	public long getTotalTimeInSeconds() {
		return totalTimeInSeconds;
	}

	public void setTotalTimeInSeconds(long totalTimeInSeconds) {
		this.totalTimeInSeconds = totalTimeInSeconds;
	}

	public void addTimeInSeconds(long time) {
		this.totalTimeInSeconds += time;
	}

	public void addDistance(long meters) {
		this.totalMeters += meters;
	}

	public long getTotalHoursParsed() {
		return totalHoursParsed;
	}

	public void setTotalHoursParsed(long totalHoursParsed) {
		this.totalHoursParsed = totalHoursParsed;
	}

	public long getTotalMinutesParsed() {
		return totalMinutesParsed;
	}

	public void setTotalMinutesParsed(long totalMinutesParsed) {
		this.totalMinutesParsed = totalMinutesParsed;
	}

	public long getTotalSecondsParsed() {
		return totalSecondsParsed;
	}

	public void setTotalSecondsParsed(long totalSecondsParsed) {
		this.totalSecondsParsed = totalSecondsParsed;
	}

	public void parseTime() {
		long hours = this.totalTimeInSeconds / 3600;
		long minutes = (this.totalTimeInSeconds - hours * 3600) / 60;
		long seconds = (this.totalTimeInSeconds - hours * 3600) - minutes * 60;

		this.totalHoursParsed = hours;
		this.totalMinutesParsed = minutes;
		this.totalSecondsParsed = seconds;
	}
}
