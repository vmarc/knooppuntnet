package kpn.planner.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AbstractBaseGraph;
import org.jgrapht.graph.WeightedMultigraph;
import org.springframework.beans.factory.annotation.Autowired;

import kpn.planner.domain.Coordinates;
import kpn.planner.domain.Route;
import kpn.planner.domain.RoutingEdge;
import kpn.planner.domain.Section;
import kpn.planner.domain.document.Analysis;
import kpn.planner.domain.document.Document;
import kpn.planner.domain.document.JsonNodeMapper;
import kpn.planner.domain.document.Map;
import kpn.planner.domain.document.Node;
import kpn.planner.domain.document.Path;
import kpn.planner.domain.document.Segment;
import kpn.planner.repository.CouchDbRepository;

public abstract class AbstractGraphService {

	private final CouchDbRepository couchDbRepository;
	private final DijkstraShortestPath<Long, RoutingEdge> dijkstraShortestPath;

	@Autowired
	protected AbstractGraphService(CouchDbRepository couchDbRepository) {
		this.couchDbRepository = couchDbRepository;
		this.dijkstraShortestPath = new DijkstraShortestPath<>(this.initializeGraph());
	}

	protected Route calculateShortestRoute(List<Long> nodes) {
		Route route = new Route();
		getNodeMappers(getNodesFromDijkstra(nodes)).forEach(mapper -> route.addSection(createSectionBetweenNodes(mapper)));

		return route;
	}

	protected List<Long> calculateShortestRoute(String routeId, Long nodeId) {
		Analysis analysis = this.couchDbRepository.getRouteDocumentById(routeId.split("-")[0]).getRoute().getAnalysis();

		List<Node> nodesByDocument = Stream.of(analysis.getStartNodes(), analysis.getStartTentacleNodes(), analysis.getEndNodes(), analysis.getEndTentacleNodes())
				.flatMap(Collection::stream).collect(Collectors.toList());

		TreeMap<Long, Node> map = new TreeMap<>();
		nodesByDocument.forEach(node -> map.put(this.dijkstraShortestPath.getPath(nodeId, node.getId()).getEdgeList().get(0).getMeters(), node));

		Node firstNode = map.get(map.firstKey());
		Node lastNode = map.get(map.lastKey());

		return Arrays.asList(firstNode.getId(), lastNode.getId());
	}

	private List<NodeMapper> getNodeMappers(List<Long> nodeList) {
		return IntStream.range(0, nodeList.size() - 1).filter(i -> !nodeList.get(i).equals(nodeList.get(i + 1)))
				.mapToObj(i -> new NodeMapper(nodeList.get(i), nodeList.get(i + 1))).collect(Collectors.toCollection(LinkedList::new));
	}

	private List<Long> getNodesFromDijkstra(List<Long> nodes) {
		List<Long> nodeList = new ArrayList<>();

		IntStream.range(0, nodes.size() - 1).mapToObj(i -> this.dijkstraShortestPath.getPath(nodes.get(i), nodes.get(i + 1)).getVertexList())
				.forEachOrdered(nodeList::addAll);

		return nodeList;
	}

	private Section createSectionBetweenNodes(NodeMapper nodeMapper) {
		RoutingEdge routingEdge = this.dijkstraShortestPath.getPath(nodeMapper.startNodeId, nodeMapper.endNodeId).getEdgeList().get(0);
		Document document = this.couchDbRepository.getRouteDocumentById(routingEdge.getRouteId());
		Section section = new Section();

		section.setStartNodeId(nodeMapper.startNodeId);
		section.setEndNodeId(nodeMapper.endNodeId);
		section.setMeters(routingEdge.getMeters());

		getCoordinatesBetweenNodes(document, routingEdge.getPathType(), section);
		getNodeNames(document, section);

		return section;
	}

	private void getNodeNames(Document document, Section section) {
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

	private void getCoordinatesBetweenNodes(Document document, String pathType, Section section) {
		Map map = document.getRoute().getAnalysis().getMap();
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

	private AbstractBaseGraph<Long, RoutingEdge> initializeGraph() {
		AbstractBaseGraph<Long, RoutingEdge> abstractBaseGraph = new WeightedMultigraph<>(RoutingEdge.class);
		List<JsonNodeMapper> list = this.couchDbRepository.getNodesFromFiles();

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
