package com.knooppuntnet.domain.pdf;

public class SectionInformation {

	private Path path;
	private PdfSection section;

	public SectionInformation() {
	}

	public SectionInformation(Path path, PdfSection section) {
		this.path = path;
		this.section = section;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public PdfSection getSection() {
		return section;
	}

	public void setSection(PdfSection section) {
		this.section = section;
	}
}
