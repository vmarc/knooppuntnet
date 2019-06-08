package kpn.planner.service;

import org.springframework.stereotype.Service;

import kpn.planner.domain.Route;
import kpn.planner.domain.pdf.PdfDocument;

@Service
public interface PdfService {

	PdfDocument createPdf(String type, String language, Route route);
}
