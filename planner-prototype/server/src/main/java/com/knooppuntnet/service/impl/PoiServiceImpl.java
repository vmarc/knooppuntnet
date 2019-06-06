package com.knooppuntnet.service.impl;

import com.knooppuntnet.domain.poi.Poi;
import com.knooppuntnet.repository.PoiRepository;
import com.knooppuntnet.service.PoiService;
import org.springframework.stereotype.Service;

@Service
public class PoiServiceImpl implements PoiService {

	private final PoiRepository poiRepository;

	public PoiServiceImpl(PoiRepository poiRepository) {
		this.poiRepository = poiRepository;
	}

	@Override
	public Poi getPoiInformation(String type, String poiId) {
		return this.poiRepository.getPoiInformation(type, poiId);
	}
}
