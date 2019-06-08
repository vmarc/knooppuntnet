package kpn.planner.service;

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

import kpn.planner.domain.NetworkType;
import kpn.planner.domain.Route;
import kpn.planner.domain.Section;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "spring.config.location=file:/kpn/conf/planner.properties")
public class PlannerServiceImplTest {

	@Rule
	public final ExpectedException expectedException = ExpectedException.none();

	@Autowired
	private PlannerService plannerService;

	@Test
	public void testCalculateShortestPath() {
		Route route = plannerService.calculateShortestPath(NetworkType.hiking, Arrays.asList(370128968L, 371723061L));
		assertEquals(370128968L, (long) route.getSections().get(0).getStartNodeId());
		assertEquals(371723061L, (long) route.getSections().get(route.getSections().size() - 1).getEndNodeId());
	}

	@Test
	public void testCalculateShortestPathWithWrongArguments() {
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("graph must contain the source vertex");
		Route route = plannerService.calculateShortestPath(NetworkType.hiking, Arrays.asList(0L, 1L));
	}

	@Test
	public void testCalculateShortestPathFromMultiline() {
		String routeId = "7583009";
		Long nodeId = 1307118521L;

		List<Long> nodes = plannerService.calculateShortestPathFromMultiline(NetworkType.hiking, routeId, nodeId);
		Route shortestRoute = plannerService.calculateShortestPath(NetworkType.hiking, nodes);

		Collections.reverse(nodes);
		Route longestRoute = plannerService.calculateShortestPath(NetworkType.hiking, nodes);

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
