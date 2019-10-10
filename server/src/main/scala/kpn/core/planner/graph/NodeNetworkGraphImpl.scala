package kpn.core.planner.graph

import kpn.core.util.Log
import kpn.shared.common.TrackPathKey
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultDirectedWeightedGraph

class NodeNetworkGraphImpl extends NodeNetworkGraph {

  private val log = Log(classOf[NodeNetworkGraphImpl])
  private val graph = new DefaultDirectedWeightedGraph[Long, TrackPathKey](classOf[TrackPathKey])
  private val dijkstraShortestPath = new DijkstraShortestPath[Long, TrackPathKey](graph)

  override def add(edge: GraphEdge): Unit = {
    graph.addVertex(edge.sourceNodeId)
    graph.addVertex(edge.sinkNodeId)
    graph.addEdge(edge.sourceNodeId, edge.sinkNodeId, edge.pathKey)
    graph.setEdgeWeight(edge.sourceNodeId, edge.sinkNodeId, edge.meters.toDouble)
  }

  override def findPath(sourceNodeId: Long, sinkNodeId: Long): Option[GraphPath] = {

    try {
      val path = dijkstraShortestPath.getPath(sourceNodeId, sinkNodeId)

      import scala.collection.JavaConverters._
      val vertexList = path.getVertexList.asScala.toList
      val edgeList = path.getEdgeList.asScala.toList

      if (vertexList.head != sourceNodeId) {
        logError(sourceNodeId, sinkNodeId, s"Expected sourceNodeId as first vertex, but found ${vertexList.head}")
        None
      }
      else if ((vertexList.size - 1) != edgeList.size) {
        logError(sourceNodeId, sinkNodeId, s"vertexList.size (${vertexList.size}) - 1 does not match edgeList.size (${edgeList.size})")
        None
      }
      else {
        val segments = vertexList.tail.zip(edgeList).map { case (vertex, pathKey) => GraphPathSegment(vertex, pathKey) }
        Some(GraphPath(sourceNodeId, segments))
      }
    }
    catch {
      case e: IllegalArgumentException =>
        logError(sourceNodeId, sinkNodeId, e.getMessage)
        None
    }
  }

  private def logError(sourceNodeId: Long, sinkNodeId: Long, message: String): Unit = {
    log.error(s"Cannot find shortest path between $sourceNodeId and $sinkNodeId: $message")
  }

}
