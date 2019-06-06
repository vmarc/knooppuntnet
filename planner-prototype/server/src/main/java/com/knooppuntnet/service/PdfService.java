package com.knooppuntnet.service;

import com.knooppuntnet.domain.Route;
import com.knooppuntnet.domain.pdf.PdfDocument;
import org.springframework.stereotype.Service;

@Service
public interface PdfService {

	PdfDocument createPdf(String type, String language, Route route);
}
