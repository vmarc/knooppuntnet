package com.knooppuntnet.controller;

import com.knooppuntnet.domain.Route;
import com.knooppuntnet.domain.pdf.PdfDocument;
import com.knooppuntnet.service.PdfService;
import com.knooppuntnet.util.RoadType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pdf")
public class PdfController {

	private final PdfService pdfService;

	public PdfController(PdfService pdfService) {
		this.pdfService = pdfService;
	}

	@PostMapping(value = "/{type}/{language}", consumes = "application/json")
	public PdfDocument pdfRoute(@PathVariable("type") String type,
								@PathVariable("language") String language,
								@RequestBody Route route) {
		return this.pdfService.createPdf(RoadType.valueOf(type).getName(), language, route);
	}
}
