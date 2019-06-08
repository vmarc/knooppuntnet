package kpn.planner.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.stereotype.Service;

import kpn.planner.domain.Coordinates;
import kpn.planner.domain.NetworkType;
import kpn.planner.domain.Route;
import kpn.planner.domain.RoutingEdge;
import kpn.planner.domain.Section;
import kpn.planner.domain.document.Analysis;
import kpn.planner.domain.document.JsonNodeMapper;
import kpn.planner.domain.document.Node;
import kpn.planner.domain.document.Path;
import kpn.planner.domain.document.RouteDocument;
import kpn.planner.domain.document.Segment;
import kpn.planner.repository.GraphRepository;

@Service
public class PlannerServiceImpl implements PlannerService {

	private final GraphRepository graphRepository;

	private final Map<NetworkType, DijkstraShortestPath<Long, RoutingEdge>> graphs;

	public PlannerServiceImpl(GraphRepository graphRepository) {
		this.graphRepository = graphRepository;
		this.graphs = new HashMap<>();
		this.graphs.put(NetworkType.cycling, new DijkstraShortestPath<>(this.initializeGraph(NetworkType.cycling)));
		this.graphs.put(NetworkType.hiking, new DijkstraShortestPath<>(this.initializeGraph(NetworkType.hiking)));
	}

	public Route calculateShortestPath(NetworkType networkType, List<Long> nodes) {
		Route route = new Route();
		getNodeMappers(getNodesFromDijkstra(networkType, nodes)).forEach(mapper -> route.addSection(createSectionBetweenNodes(networkType, mapper)));
		return route;
	}

	public List<Long> calculateShortestPathFromMultiline(NetworkType networkType, String routeId, Long nodeId) {
		Analysis analysis = this.graphRepository.getRouteDocumentById(routeId.split("-")[0]).getRoute().getAnalysis();

		List<Node> nodesByDocument = Stream.of(analysis.getStartNodes(), analysis.getStartTentacleNodes(), analysis.getEndNodes(), analysis.getEndTentacleNodes())
				.flatMap(Collection::stream).collect(Collectors.toList());

		TreeMap<Long, Node> map = new TreeMap<>();
		nodesByDocument.forEach(node -> {
			DijkstraShortestPath dijstra = this.graphs.get(networkType);
			GraphPath<Long, RoutingEdge> path = dijstra.getPath(nodeId, node.getId());
			Long meters = path.getEdgeList().get(0).getMeters();
			map.put(meters, node);
		});

		Node firstNode = map.get(map.firstKey());
		Node lastNode = map.get(map.lastKey());

		return Arrays.asList(firstNode.getId(), lastNode.getId());
	}

	private List<NodeMapper> getNodeMappers(List<Long> nodeList) {
		return IntStream.range(0, nodeList.size() - 1).filter(i -> !nodeList.get(i).equals(nodeList.get(i + 1)))
				.mapToObj(i -> new NodeMapper(nodeList.get(i), nodeList.get(i + 1))).collect(Collectors.toCollection(LinkedList::new));
	}

	private List<Long> getNodesFromDijkstra(NetworkType networkType, List<Long> nodes) {
		List<Long> nodeList = new ArrayList<>();

		IntStream.range(0, nodes.size() - 1).mapToObj(i -> graphs.get(networkType).getPath(nodes.get(i), nodes.get(i + 1)).getVertexList())
				.forEachOrdered(nodeList::addAll);

		return nodeList;
	}

	private Section createSectionBetweenNodes(NetworkType networkType, NodeMapper nodeMapper) {
		RoutingEdge routingEdge = this.graphs.get(networkType).getPath(nodeMapper.startNodeId, nodeMapper.endNodeId).getEdgeList().get(0);
		RouteDocument document = this.graphRepository.getRouteDocumentById(routingEdge.getRouteId());
		Section section = new Section();

		section.setStartNodeId(nodeMapper.startNodeId);
		section.setEndNodeId(nodeMapper.endNodeId);
		section.setMeters(routingEdge.getMeters());

		getCoordinatesBetweenNodes(document, routingEdge.getPathType(), section);
		getNodeNames(document, section);

		return section;
	}

	private void getNodeNames(RouteDocument document, Section section) {
		Analysis analysis = document.getRoute().getAnalysis();

		List<Node> nodeList = Stream.of(analysis.getStartNodes(), analysis.getEndNodes(), analysis.getStartTentacleNodes(), analysis.getEndTentacleNodes())
				.flatMap(Collection::stream).collect(Collectors.toList());

		Node start = nodeList.stream().filter(n -> n.getId().equals(section.getStartNodeId())).findFirst().orElse(null);
		Node end = nodeList.stream().filter(n -> n.getId().equals(section.getEndNodeId())).findAny().orElse(null);

		if (start != null & end != null) {
			section.setStartNode(start.getName());
			section.setEndNode(end.getName());

			section.addWayPoint(section.getStartNode(), new Coordinates(start.getLat(), start.getLon()));
			section.addWayPoint(section.getEndNode(), new Coordinates(end.getLat(), end.getLon()));
		}
	}

	private void getCoordinatesBetweenNodes(RouteDocument document, String pathType, Section section) {
		kpn.planner.domain.document.Map map = document.getRoute().getAnalysis().getMap();
		List<Segment> segments = new ArrayList<>();

		switch (pathType) {
			case "forward":
				segments = map.getForwardPath().getSegments();
				break;
			case "backward":
				segments = map.getBackwardPath().getSegments();
				break;
			case "start": {
				segments = getSegmentsForTentaclePaths(section, map.getStartTentaclePaths());
				break;
			}
			case "end": {
				segments = getSegmentsForTentaclePaths(section, map.getEndTentaclePaths());
				break;
			}
		}

		segments.forEach(segment -> section.addCoordinates(segment.getTrackPoints()));
	}

	private List<Segment> getSegmentsForTentaclePaths(Section section, List<Path> tentaclePaths) {
		Optional<Path> path = tentaclePaths.stream().filter(p -> p.getStartNodeId().equals(section.getStartNodeId()))
				.filter(p -> p.getEndNodeId().equals(section.getEndNodeId())).findAny();

		if (!path.isPresent()) {
			path = tentaclePaths.stream().filter(p -> p.getStartNodeId().equals(section.getEndNodeId())).filter(p -> p.getEndNodeId().equals(section.getStartNodeId()))
					.findAny();
		}

		return path.map(Path::getSegments).orElse(null);
	}

	private AbstractBaseGraph<Long, RoutingEdge> initializeGraph(NetworkType networkType) {
		AbstractBaseGraph<Long, RoutingEdge> abstractBaseGraph = new WeightedMultigraph<>(RoutingEdge.class);
		List<JsonNodeMapper> list = this.graphRepository.getNodesFromFiles(networkType);

		list.forEach(jsonNodeMapper -> {
			Long startNodeId = jsonNodeMapper.getValues().get(0);
			Long endNodeId = jsonNodeMapper.getValues().get(1);
			Long meters = jsonNodeMapper.getValues().get(2);

			String routeId = jsonNodeMapper.getKey().get(1);
			String pathType = jsonNodeMapper.getKey().get(2);
			Long pathIndex = Long.valueOf(jsonNodeMapper.getKey().get(3));

			abstractBaseGraph.addVertex(startNodeId);
			abstractBaseGraph.addVertex(endNodeId);

			RoutingEdge edge = abstractBaseGraph.addEdge(startNodeId, endNodeId);
			edge.setPathIndex(pathIndex);
			edge.setPathType(pathType);
			edge.setRouteId(routeId);
			edge.setMeters(meters);

			abstractBaseGraph.setEdgeWeight(edge, Double.valueOf(meters));
		});

		return abstractBaseGraph;
	}

	private class NodeMapper {
		private Long startNodeId;
		private Long endNodeId;

		NodeMapper(Long startNodeId, Long endNodeId) {
			this.startNodeId = startNodeId;
			this.endNodeId = endNodeId;
		}
	}
}
