package kpn.planner.service;

import org.springframework.stereotype.Service;

import kpn.planner.domain.poi.Poi;
import kpn.planner.repository.PoiRepository;

@Service
public class PoiServiceImpl implements PoiService {

	private final PoiRepository poiRepository;

	public PoiServiceImpl(PoiRepository poiRepository) {
		this.poiRepository = poiRepository;
	}

	@Override
	public Poi getPoi(String type, String poiId) {
		return this.poiRepository.getPoi(type, poiId);
	}
}
