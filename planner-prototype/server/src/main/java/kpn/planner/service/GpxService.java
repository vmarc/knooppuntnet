package kpn.planner.service;

import javax.servlet.http.HttpServletResponse;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import kpn.planner.domain.Route;
import kpn.planner.domain.Section;

@Service
public interface GpxService {

	void createGpx(Route route, HttpServletResponse response);

	Document getDocumentForRoute(Route route) throws ParserConfigurationException;

	Document getDocumentForSection(Section section) throws ParserConfigurationException;
}
