package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.route.Backward
import kpn.api.common.route.Both
import kpn.api.common.route.Forward
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.analysis.route.OneWayAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.segment.FragmentMap
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.analyzer.engine.analysis.route.segment.PavedUnpavedSplitter
import kpn.server.analyzer.engine.analysis.route.segment.SegmentFragment
import org.jgrapht.alg.shortestpath.DijkstraShortestPath
import org.jgrapht.graph.DirectedWeightedPseudograph

import scala.collection.mutable.ListBuffer

object RoutePathAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RoutePathAnalyzer(context).analyze
  }
}

class RoutePathAnalyzer(context: RouteAnalysisContext) {

  private val facts: ListBuffer[Fact] = ListBuffer[Fact]()
  facts ++= context.facts

  def analyze: RouteAnalysisContext = {
    val fragmentMap = contextFragmentMap()
    val nodeAnalysis = routeNodeAnalysis()
    val routeNodes = nodeAnalysis.startNodes ++ nodeAnalysis.endNodes
    val nodeIds = routeNodes.map(_.id)
    //    println(s"nodeIds=$nodeIds")
    //
    //    fragmentMap.all.foreach { fragment =>
    //      val s = fragment.nodes.head.id
    //      val e = fragment.nodes.last.id
    //      println(s"   start=$s, end=$e ")
    //    }

    val paths = routeNodes.combinations(2).toSeq.flatMap { case Seq(startRouteNode, endRouteNode) =>
      Seq(
        analyzePath(fragmentMap, nodeIds, startRouteNode, endRouteNode),
        analyzePath(fragmentMap, nodeIds, endRouteNode, startRouteNode)
      ).flatten
    }

    context.copy(structure = context.structure.map(_.copy(paths = Some(paths))))
  }

  private def analyzePath(fragmentMap: FragmentMap, nodeIds: Seq[Long], startRouteNode: RouteNode, endRouteNode: RouteNode): Option[Path] = {

    val otherNodeIds = nodeIds.filter(id => id != startRouteNode.id && id != endRouteNode.id)

    //    println(s"start=${startRouteNode.id}, end=${endRouteNode.id}, other=${otherNodeIds}, fragments=${fragmentMap.ids}")

    val graph = new DirectedWeightedPseudograph[Long, SegmentFragment](classOf[SegmentFragment])
    val dijkstraShortestPath = new DijkstraShortestPath[Long, SegmentFragment](graph)

    fragmentMap.ids.foreach { fragmentId =>
      val fragment = fragmentMap(fragmentId)
      val fragmentStartNodeId = fragment.nodes.head.id
      val fragmentEndNodeId = fragment.nodes.last.id

      if (!otherNodeIds.contains(fragmentStartNodeId) && !otherNodeIds.contains(fragmentEndNodeId)) {

        val direction = if (context.scopedNetworkType.networkType == NetworkType.cycling) {
          new OneWayAnalyzer(fragment.way).direction
        }
        else {
          Both
        }

        if ((direction == Both || direction == Forward) && !fragment.role.contains("backward")) {
          graph.addVertex(fragmentStartNodeId)
          graph.addVertex(fragmentEndNodeId)
          graph.addEdge(fragmentStartNodeId, fragmentEndNodeId, SegmentFragment(fragment))
          graph.setEdgeWeight(fragmentStartNodeId, fragmentEndNodeId, fragment.meters)
          // println(s"   edge=${"F" + fragmentId}, start=$fragmentStartNodeId, end=$fragmentEndNodeId, weight=${fragment.meters}")
        }

        if ((direction == Both || direction == Backward) && !fragment.role.contains("forward")) {
          graph.addVertex(fragmentEndNodeId)
          graph.addVertex(fragmentStartNodeId)
          graph.addEdge(fragmentEndNodeId, fragmentStartNodeId, SegmentFragment(fragment, reversed = true))
          graph.setEdgeWeight(fragmentEndNodeId, fragmentStartNodeId, fragment.meters)
          // println(s"   edge=${"B" + fragmentId}, start=$fragmentEndNodeId, end=$fragmentStartNodeId, weight=${fragment.meters}")
        }
      }
    }


    if (!graph.containsVertex(startRouteNode.id) || !graph.containsVertex(endRouteNode.id)) {
      None
    }
    else {
      val path = dijkstraShortestPath.getPath(startRouteNode.id, endRouteNode.id)
      if (path == null) {
        None
      }
      else {
        import scala.jdk.CollectionConverters._
        val vertexList = path.getVertexList.asScala.toList
        val segmentFragments = path.getEdgeList.asScala.toList
        val segments = PavedUnpavedSplitter.split(segmentFragments)

        Some(
          Path(
            Some(startRouteNode),
            Some(endRouteNode),
            startRouteNode.id,
            endRouteNode.id,
            segments,
            oneWay = false, // TODO take this out again ??
            broken = false
          )
        )
      }

    }
  }

  private def contextFragmentMap(): FragmentMap = {
    context.fragmentMap.getOrElse(throw new IllegalStateException("fragmentMap required before route path analysis"))
  }

  private def routeNodeAnalysis(): RouteNodeAnalysis = {
    context.routeNodeAnalysis.getOrElse(throw new IllegalStateException("route node analysis required before route path analysis"))
  }

}
