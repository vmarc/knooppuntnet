package kpn.core.engine.analysis.route.segment

import kpn.core.engine.analysis.route.RouteNode
import kpn.core.engine.analysis.route.RouteNodeAnalysis
import kpn.core.engine.analysis.route.RouteNodeAnalysisFormatter
import kpn.core.engine.analysis.route.RouteStructure
import kpn.core.engine.analysis.route.TentacleAnalyzer
import kpn.core.engine.analysis.route.UnusedSegmentAnalyzer
import kpn.core.engine.analysis.route.segment.SegmentDirection.Forward
import kpn.core.util.Log
import kpn.shared.NetworkType
import kpn.shared.data.Node

class SegmentAnalyzer(
  networkType: NetworkType,
  routeId: Long,
  allFragments: Seq[Fragment],
  routeNodeAnalysis: RouteNodeAnalysis
) {

  private val log = Log(classOf[SegmentAnalyzer])

  private val allRouteNodes: Set[RouteNode] = (routeNodeAnalysis.startNodes ++ routeNodeAnalysis.endNodes).toSet
  private val allNodes: Set[Node] = allRouteNodes.map(_.node)

  private val segmentFinder = new SegmentFinder(networkType, allRouteNodes, allNodes)

  def structure: RouteStructure = {

    if (log.isDebugEnabled) {
      logStart(allFragments)
    }

    val forwardPath = findForwardPath()
    val backwardPath = findBackwardPath()
    val usedSegments1 = forwardPath.toSeq.flatMap(_.segments) ++ backwardPath.toSeq.flatMap(_.segments)
    val usedFragments1 = usedSegments1.flatMap(_.fragments).map(_.fragment)
    val availableFragments = (allFragments.toSet -- usedFragments1.toSet).toSeq
    val startTentaclePaths = new TentacleAnalyzer(segmentFinder, availableFragments, routeNodeAnalysis.startNodes.map(_.node)).findTentacles
    val endTentaclePaths = new TentacleAnalyzer(segmentFinder, availableFragments, routeNodeAnalysis.endNodes.map(_.node)).findTentacles
    val unusedSegments = {
      val used = forwardPath.toSeq.flatMap(_.segments) ++ backwardPath.toSeq.flatMap(_.segments) ++ startTentaclePaths.flatMap(_.segments) ++ endTentaclePaths.flatMap(_.segments)
      findUnusedSegments(used)
    }

    RouteStructure(
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
    segmentFinder.find(allFragments, direction, source, target)
  }

  private def findUnusedSegments(usedSegments: Iterable[Segment]): Seq[Segment] = {
    new UnusedSegmentAnalyzer(usedSegments, allFragments).find
  }

  private def logStart(fragments: Seq[Fragment]): Unit = {
    val b = new StringBuilder
    b.append("Start\n")
    b.append(s"  fragments (${fragments.size}):\n")
    fragments.foreach { f =>
      b.append(s"    ${new FragmentFormatter(f).string}\n")
    }

    b.append(s"  nodes=${new RouteNodeAnalysisFormatter(routeNodeAnalysis).string}\n")

    val message = b.toString()
    log.debug(message)
  }
}
