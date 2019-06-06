package com.knooppuntnet.service.impl;

import com.knooppuntnet.domain.poi.Poi;
import com.knooppuntnet.repository.PoiRepository;
import org.ektorp.DocumentNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class PoiServiceImplTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private PoiServiceImpl poiService;

	@Before
	public void setup() {
		this.poiService = new PoiServiceImpl(new PoiRepository() {});
	}

	//@Test
	public void testGetPoiInformation() {
		Poi poi = this.poiService.getPoiInformation("way", "50610511");

		assertEquals(poi.getElementType(), "way");
		assertEquals(poi.getElementId(), Long.valueOf(50610511));
		assertEquals(poi.getLatitude(), Double.valueOf(51.4626506));
		assertEquals(poi.getLongitude(), Double.valueOf(4.4514157));
		//assertEquals(poi.getLayers().get(0), "heritage");
	}

	@Test
	public void testGetPoiInformationWithWrongArguments() {
		expectedException.expect(DocumentNotFoundException.class);

		Poi poi = this.poiService.getPoiInformation("invalid", "50610511");
	}
}
