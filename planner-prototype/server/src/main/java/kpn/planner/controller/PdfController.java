package kpn.planner.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kpn.planner.domain.Route;
import kpn.planner.domain.pdf.PdfDocument;
import kpn.planner.service.PdfService;
import kpn.planner.util.RoadType;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

	private final PdfService pdfService;

	public PdfController(PdfService pdfService) {
		this.pdfService = pdfService;
	}

	@PostMapping(value = "/{type}/{language}", consumes = "application/json")
	public PdfDocument pdfRoute(@PathVariable("type") String type, @PathVariable("language") String language, @RequestBody Route route) {
		return this.pdfService.createPdf(RoadType.valueOf(type).getName(), language, route);
	}
}
