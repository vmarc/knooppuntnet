package kpn.planner.service.impl;

import org.springframework.stereotype.Service;

import kpn.planner.domain.poi.Poi;
import kpn.planner.repository.PoiRepository;
import kpn.planner.service.PoiService;

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
