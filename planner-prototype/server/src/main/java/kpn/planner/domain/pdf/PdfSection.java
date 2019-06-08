package kpn.planner.domain.pdf;

public class PdfSection {

	private String startNode;
	private String endNode;
	private Long meters;

	public PdfSection() {
	}

	public PdfSection(String startNode, String endNode, Long meters) {
		this.startNode = startNode;
		this.endNode = endNode;
		this.meters = meters;
	}

	public String getStartNode() {
		return startNode;
	}

	public void setStartNode(String startNode) {
		this.startNode = startNode;
	}

	public String getEndNode() {
		return endNode;
	}

	public void setEndNode(String endNode) {
		this.endNode = endNode;
	}

	public Long getMeters() {
		return meters;
	}

	public void setMeters(Long meters) {
		this.meters = meters;
	}
}
