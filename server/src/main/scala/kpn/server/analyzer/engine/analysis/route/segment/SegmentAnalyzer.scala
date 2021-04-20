package kpn.server.analyzer.engine.analysis.route.segment

import kpn.api.common.data.Node
import kpn.api.custom.NetworkType
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.FreePathAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteNode
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysisFormatter
import kpn.server.analyzer.engine.analysis.route.RouteStructure
import kpn.server.analyzer.engine.analysis.route.TentacleAnalyzer
import kpn.server.analyzer.engine.analysis.route.UnusedSegmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.SegmentDirection.Forward

class SegmentAnalyzer(
  networkType: NetworkType,
  routeId: Long,
  loop: Boolean,
  fragmentMap: FragmentMap,
  routeNodeAnalysis: RouteNodeAnalysis
) {

  private val log = Log(classOf[SegmentAnalyzer])

  private val allRouteNodes: Set[RouteNode] = routeNodeAnalysis.usedNodes.toSet
  private val allNodes: Set[Node] = allRouteNodes.map(_.node)

  private val segmentFinder = new SegmentFinder(fragmentMap, networkType, allRouteNodes, allNodes, loop)
  private val allFragmentIds = fragmentMap.ids.toSet

  def structure: RouteStructure = {

    if (log.isDebugEnabled) {
      logStart()
    }

    val forwardPath = findForwardPath()
    val backwardPath = findBackwardPath()
    val usedSegments = forwardPath.toSeq.flatMap(_.segments) ++ backwardPath.toSeq.flatMap(_.segments)
    val usedFragmentIds = usedSegments.flatMap(_.fragments).map(_.fragment.id).toSet
    val availableFragmentIds = allFragmentIds.diff(usedFragmentIds)
    val startTentaclePaths = new TentacleAnalyzer(segmentFinder, availableFragmentIds, routeNodeAnalysis.startNodes.map(_.node)).findTentacles
    val endTentaclePaths = new TentacleAnalyzer(segmentFinder, availableFragmentIds, routeNodeAnalysis.endNodes.map(_.node)).findTentacles

    val freePaths = new FreePathAnalyzer(segmentFinder, allFragmentIds, routeNodeAnalysis.freeNodes.map(_.node)).findTentacles

    val unusedSegments = {
      val used = freePaths.flatMap(_.segments) ++
        forwardPath.toSeq.flatMap(_.segments) ++
        backwardPath.toSeq.flatMap(_.segments) ++
        startTentaclePaths.flatMap(_.segments) ++
        endTentaclePaths.flatMap(_.segments)
      findUnusedSegments(used)
    }

    RouteStructure(
      freePaths,
      forwardPath,
      backwardPath,
      startTentaclePaths,
      endTentaclePaths,
      unusedSegments
    )
  }

  case class PathNodes(sourceNode: RouteNode, targetNode: RouteNode)

  private def findForwardPath(): Option[Path] = {
    findPath(routeNodeAnalysis.startNodes, routeNodeAnalysis.endNodes)
  }

  private def findBackwardPath(): Option[Path] = {
    findPath(routeNodeAnalysis.endNodes, routeNodeAnalysis.startNodes)
  }

  private def findPath(sourceNodes: Seq[RouteNode], targetNodes: Seq[RouteNode]): Option[Path] = {
    val startEndCombinations = sourceNodes.flatMap(sourceNode => targetNodes.map(targetNode => PathNodes(sourceNode, targetNode)))
    val paths = startEndCombinations.flatMap { path =>
      findPath2(Forward, path.sourceNode.node, path.targetNode.node)
    }
    PathSelector.select(paths)
  }

  private def findPath(direction: SegmentDirection.Value, startNode: Option[RouteNode], endNode: Option[RouteNode]): Option[Path] = {
    if (startNode.isDefined && endNode.isDefined) {
      findPath2(Forward, startNode.get.node, endNode.get.node)
    }
    else {
      None
    }
  }

  private def findPath2(direction: SegmentDirection.Value, source: Node, target: Node): Option[Path] = {
    segmentFinder.find(allFragmentIds, direction, source, target)
  }

  private def findUnusedSegments(usedSegments: Iterable[Segment]): Seq[Segment] = {
    new UnusedSegmentAnalyzer(usedSegments, fragmentMap).find
  }

  private def logStart(): Unit = {
    val b = new StringBuilder
    b.append("Start\n")
    b.append(s"  fragments (${fragmentMap.size}):\n")
    fragmentMap.all.foreach { f =>
      b.append(s"    ${new FragmentFormatter(f).string}\n")
    }
    b.append(s"  nodes=${new RouteNodeAnalysisFormatter(routeNodeAnalysis).string}\n")
    val message = b.toString()
    log.debug(message)
  }
}
