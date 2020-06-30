package kpn.core.planner.graph

import kpn.api.common.common.TrackPathKey
import kpn.core.util.Log
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DefaultDirectedWeightedGraph

class NodeNetworkGraphImpl extends NodeNetworkGraph {

  private val log = Log(classOf[NodeNetworkGraphImpl])
  private val graph = new DefaultDirectedWeightedGraph[String, String](classOf[String])
  private val dijkstraShortestPath = new DijkstraShortestPath[String, String](graph)

  override def add(edge: GraphEdge): Unit = {
    val source = edge.sourceNodeId.toString
    val sink = edge.sinkNodeId.toString
    val via = edge.pathKey.key
    val distance = edge.meters.toDouble / 2

    graph.addVertex(source)
    graph.addVertex(sink)
    graph.addVertex(via)

    graph.addEdge(source, via, via + "+a")
    graph.setEdgeWeight(source, via, distance)

    graph.addEdge(via, sink, via + "+b")
    graph.setEdgeWeight(via, sink, distance)
  }

  override def findPath(source: String, sink: String): Option[GraphPath] = {

    try {
      val path = dijkstraShortestPath.getPath(source, sink)

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
            val pathKeyParts = pathKey.split('+')
            val key = TrackPathKey(pathKeyParts(0).toLong, pathKeyParts(1).toLong)
            GraphPathSegment(vertex, key)
          }
          Some(GraphPath(source, segments))
        }
      }
    }
    catch {
      case e: IllegalArgumentException =>
        logError(source, sink, e.getMessage)
        None
    }
  }

  private def logError(source: String, sink: String, message: String): Unit = {
    log.error(s"Cannot find shortest path between $source and $sink: $message")
  }

}
