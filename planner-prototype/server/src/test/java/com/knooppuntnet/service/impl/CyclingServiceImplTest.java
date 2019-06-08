package com.knooppuntnet.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.knooppuntnet.domain.Route;
import com.knooppuntnet.domain.Section;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=file:/kpn/conf/planner-prototype.properties")
public class CyclingServiceImplTest {

	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Autowired
	private CyclingServiceImpl cyclingService;

	@Test
	public void testCalculateShortestPath() {
		Route route = cyclingService.calculateShortestRoute(Arrays.asList(304735214L, 322765579L));

		assertEquals(304735214L, (long) route.getSections().get(0).getStartNodeId());
		assertEquals(322765579L, (long) route.getSections().get(route.getSections().size() - 1).getEndNodeId());
	}

	@Test
	public void testCalculateShortestPathWithWrongArguments() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("graph must contain the source vertex");

		Route route = cyclingService.calculateShortestRoute(Arrays.asList(0L, 1L));
	}

	@Test
	public void testCalculateShortestPathFromMultiline() {
		String routeId = "57111";
		Long nodeId = 253818931L;

		List<Long> nodes = cyclingService.calculateShortestPathFromMultiline(routeId, nodeId);
		Route shortestRoute = cyclingService.calculateShortestPath(nodes);

		Collections.reverse(nodes);
		Route longestRoute = cyclingService.calculateShortestPath(nodes);

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
