package kpn.planner.domain;

import java.util.ArrayList;
import java.util.List;

public class Route {

	private List<Section> sections = new ArrayList<>();

	public Route() {
	}

	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
	}

	public void addSection(Section section) {
		this.sections.add(section);
	}
}
