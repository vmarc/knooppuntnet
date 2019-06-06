package com.knooppuntnet.service.impl;

import com.knooppuntnet.domain.Route;
import com.knooppuntnet.domain.Section;
import com.knooppuntnet.service.GpxService;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;

@Service
public class GpxServiceImpl implements GpxService {

	private static final DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();

	public void createGpx(Route route, HttpServletResponse response) {
		try {
			response.setContentType("application/gpx");

			Document document = getDocumentForRoute(route);

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource domSource = new DOMSource(document);
			StreamResult streamResult = new StreamResult(response.getOutputStream());

			transformer.transform(domSource, streamResult);
		} catch (IOException | ParserConfigurationException | TransformerException e) {
			e.printStackTrace();
		}
	}

	public Document getDocumentForRoute(Route route) throws ParserConfigurationException {
		Document document = getDocument();
		Element root = getRootElement(document);

		route.getSections().forEach(section -> getBackAndForthWayPoints(document, root, section));

		Element trk = document.createElement("trk");
		root.appendChild(trk);
		Element trkSegment = document.createElement("trkseg");
		trk.appendChild(trkSegment);

		route.getSections().forEach(section -> getElementsFromCoordinates(section, document, trkSegment));

		return document;
	}

	public Document getDocumentForSection(Section section) throws ParserConfigurationException {
		Document document = getDocument();
		Element root = getRootElement(document);

		getBackAndForthWayPoints(document, root, section);

		Element trk = document.createElement("trk");
		root.appendChild(trk);

		Element trkSegment = document.createElement("trkseg");
		trk.appendChild(trkSegment);

		getElementsFromCoordinates(section, document, trkSegment);

		return document;
	}

	private Document getDocument() throws ParserConfigurationException {
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		return documentBuilder.newDocument();
	}

	private Element getRootElement(Document document) {
		Element root = document.createElement("gpx");
		Attr rootAttr = document.createAttribute("version");
		rootAttr.setValue("1.1");
		root.setAttributeNode(rootAttr);
		document.appendChild(root);
		return root;
	}

	private void getBackAndForthWayPoints(Document document, Element root, Section section) {
		Element start = document.createElement("wpt");
		root.appendChild(start);
		start.setAttribute("lat", section.getWaypoints().get(section.getStartNode()).getLat().toString());
		start.setAttribute("lon", section.getWaypoints().get(section.getStartNode()).getLon().toString());

		Element wptName = document.createElement("name");
		wptName.appendChild(document.createTextNode(section.getStartNode()));
		start.appendChild(wptName);

		Element end = document.createElement("wpt");
		root.appendChild(end);
		end.setAttribute("lat", section.getWaypoints().get(section.getEndNode()).getLat().toString());
		end.setAttribute("lon", section.getWaypoints().get(section.getEndNode()).getLon().toString());

		Element wptEnd = document.createElement("name");
		wptEnd.appendChild(document.createTextNode(section.getEndNode()));
		end.appendChild(wptEnd);
	}

	private void getElementsFromCoordinates(Section section, Document document, Element trkSegment) {
		section.getCoordinates().stream().distinct().forEach(coordinates -> {
			Element trkpt = document.createElement("trkpt");
			trkpt.setAttribute("lat", coordinates.getLat().toString());
			trkpt.setAttribute("lon", coordinates.getLon().toString());

			trkSegment.appendChild(trkpt);

			Element name = document.createElement("name");
			name.appendChild(document.createTextNode(""));
			trkpt.appendChild(name);
		});
	}
}
