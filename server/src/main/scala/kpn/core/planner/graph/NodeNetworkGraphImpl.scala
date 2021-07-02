package kpn.core.planner.graph

import kpn.api.common.common.TrackPathKey
import kpn.core.util.Log
import kpn.core.util.Util
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.AsWeightedGraph
import org.jgrapht.graph.DefaultDirectedWeightedGraph

class NodeNetworkGraphImpl extends NodeNetworkGraph {

  private val log = Log(classOf[NodeNetworkGraphImpl])
  private val graphFull = new DefaultDirectedWeightedGraph[String, String](classOf[String])
  private val dijkstraShortestPathFull = new DijkstraShortestPath[String, String](graphFull)

  private var proposedRouteIds: Set[Long] = Set.empty

  private val weightFunction: java.util.function.Function[String, java.lang.Double] = (edge: String) => {
    val proposed = isProposed(edge)
    if (proposed) {
      Long.MaxValue
    }
    else {
      graphFull.getEdgeWeight(edge)
    }
  }

  private def isProposed(edge: String): Boolean = {
    if (edge.contains(".")) {
      val edgeParts = edge.split("\\.")
      if (edgeParts.nonEmpty) {
        val routeIdString = edgeParts.head
        if (Util.isDigits(routeIdString)) {
          proposedRouteIds.contains(routeIdString.toLong)
        }
        else {
          false
        }
      }
      else {
        false
      }
    }
    else {
      false
    }
  }

  private val graphWithoutProposed: AsWeightedGraph[String, String] = GraphBuilder.weigthedGraph(graphFull, weightFunction)
  private val dijkstraShortestPathWithoutProposed = {
    new DijkstraShortestPath[String, String](graphWithoutProposed)
  }

  override def add(edge: GraphEdge): Unit = {
    if (edge.proposed) {
      proposedRouteIds = proposedRouteIds ++ Seq(edge.pathKey.routeId)
    }
    val source = edge.sourceNodeId.toString
    val sink = edge.sinkNodeId.toString
    val via = edge.pathKey.key
    val distance = edge.meters.toDouble / 2

    graphFull.addVertex(source)
    graphFull.addVertex(sink)
    graphFull.addVertex(via)

    graphFull.addEdge(source, via, via + "+a")
    graphFull.setEdgeWeight(source, via, distance)

    graphFull.addEdge(via, sink, via + "+b")
    graphFull.setEdgeWeight(via, sink, distance)
  }

  override def findPath(source: String, sink: String): Option[GraphPath] = {

    try {
      val path = if (proposed) {
        dijkstraShortestPathFull.getPath(source, sink)
      }
      else {
        dijkstraShortestPathWithoutProposed.getPath(source, sink)
      }

      if (path == null) {
        logError(source, sink, "dijkstra")
        None
      }
      else {
        import scala.jdk.CollectionConverters._
        val vertexList = path.getVertexList.asScala.toList
        val edgeList = path.getEdgeList.asScala.toList

        if (vertexList.head != source) {
          logError(source, sink, s"Expected $source as first vertex, but found ${vertexList.head}")
          None
        }
        else if ((vertexList.size - 1) != edgeList.size) {
          logError(source, sink, s"vertexList.size (${vertexList.size}) - 1 does not match edgeList.size (${edgeList.size})")
          None
        }
        else {
          val segments = vertexList.tail.zip(edgeList).map { case (vertex, pathKey) =>
            val pathKeyParts = pathKey.split(Array('.', '+'))
            val key = TrackPathKey(pathKeyParts(0).toLong, pathKeyParts(1).toLong)
            GraphPathSegment(vertex, key)
          }
          Some(GraphPath(source, segments))
        }
      }
    }
    catch {
      case e: Exception =>
        logError(source, sink, e.getMessage)
        None
    }
  }

  private def logError(source: String, sink: String, message: String): Unit = {
    log.error(s"Cannot find shortest path between $source and $sink: $message")
  }
}
