package com.knooppuntnet.service.impl;

import com.knooppuntnet.domain.Route;
import com.knooppuntnet.domain.Section;
import com.knooppuntnet.repository.HikingRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class HikingServiceImplTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	private HikingServiceImpl hikingService;

	@Before
	public void setup() {
		this.hikingService = new HikingServiceImpl(new HikingRepository());
	}

	@Test
	public void testCalculateShortestPath() {
		Route route = hikingService.calculateShortestRoute(Arrays.asList(370128968L, 371723061L));

		assertEquals(370128968L, (long) route.getSections().get(0).getStartNodeId());
		assertEquals(371723061L, (long) route.getSections().get(route.getSections().size() - 1).getEndNodeId());
	}

	@Test
	public void testCalculateShortestPathWithWrongArguments() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("graph must contain the source vertex");

		Route route = hikingService.calculateShortestRoute(Arrays.asList(0L, 1L));
	}

	@Test
	public void testCalculateShortestPathFromMultiline() {
		String routeId = "7583009";
		Long nodeId = 1307118521L;

		List<Long> nodes = hikingService.calculateShortestPathFromMultiline(routeId, nodeId);
		Route shortestRoute = hikingService.calculateShortestPath(nodes);

		Collections.reverse(nodes);
		Route longestRoute = hikingService.calculateShortestPath(nodes);

		Long totalMetersShortestRoute = 0L;
		for (Section section : shortestRoute.getSections()) {
			totalMetersShortestRoute += section.getMeters();
		}

		Long totalMetersLongestRoute = 0L;
		for (Section section : longestRoute.getSections()) {
			totalMetersLongestRoute += section.getMeters();
		}

		assertTrue(totalMetersShortestRoute <= totalMetersLongestRoute);
	}

}