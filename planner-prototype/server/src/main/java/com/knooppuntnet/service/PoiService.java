package com.knooppuntnet.service;

import com.knooppuntnet.domain.poi.Poi;
import org.springframework.stereotype.Service;

@Service
public interface PoiService {

	Poi getPoiInformation(String type, String poiId);
}
