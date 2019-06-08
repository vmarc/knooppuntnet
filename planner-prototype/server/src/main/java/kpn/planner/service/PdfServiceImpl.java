package kpn.planner.service;

import java.io.StringWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;

import kpn.planner.domain.Route;
import kpn.planner.domain.Section;
import kpn.planner.domain.pdf.MapMatching;
import kpn.planner.domain.pdf.PdfDocument;
import kpn.planner.domain.pdf.PdfSection;
import kpn.planner.domain.pdf.SectionInformation;

@Service
public class PdfServiceImpl implements PdfService {

	private static final String KEY = "xxx";
	private static final String VERSION = "https://graphhopper.com/api/1/match?key=" + KEY;
	private final GpxService gpxService;

	public PdfServiceImpl(GpxService gpxService) {
		this.gpxService = gpxService;
	}

	public PdfDocument createPdf(String type, String language, Route route) {
		PdfDocument pdfDocument = new PdfDocument();
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = getHttpHeaders();
		StringBuilder query = getQueryString(type, language);

		route.getSections().forEach(section -> {
			try {
				String output = getStringOutput(section);

				MapMatching mapMatching = getMapMatching(restTemplate, headers, query, output);

				if (mapMatching != null) {
					PdfSection pdfSection = new PdfSection(section.getStartNode(), section.getEndNode(), section.getMeters());
					pdfDocument.addSectionInformation(new SectionInformation(mapMatching.getPath(), pdfSection));
				}
			} catch (ParserConfigurationException | TransformerException e) {
				e.printStackTrace();
			}
			pdfDocument.getInformations().forEach(sectionInformation -> {
				pdfDocument.addTimeInSeconds(sectionInformation.getPath().getTime() / 1000);
				pdfDocument.addDistance(sectionInformation.getSection().getMeters());
			});
			pdfDocument.parseTime();
		});

		return pdfDocument;
	}

	private MapMatching getMapMatching(RestTemplate restTemplate, HttpHeaders headers, StringBuilder query, String output) {
		HttpEntity<String> request = new HttpEntity<>(output, headers);
		return restTemplate.postForObject(VERSION + query, request, MapMatching.class);
	}

	private String getStringOutput(Section section) throws ParserConfigurationException, TransformerException {
		Document document = gpxService.getDocumentForSection(section);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();

		StringWriter writer = new StringWriter();

		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.transform(new DOMSource(document), new StreamResult(writer));

		return writer.getBuffer().toString().replaceAll("[\n\r]", "");
	}

	private HttpHeaders getHttpHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_XML);
		return headers;
	}

	private StringBuilder getQueryString(String type, String language) {
		StringBuilder query = new StringBuilder("&type=json&instructions=true");
		query.append("&locale=").append(language);
		query.append("&vehicle").append(type);
		return query;
	}
}
