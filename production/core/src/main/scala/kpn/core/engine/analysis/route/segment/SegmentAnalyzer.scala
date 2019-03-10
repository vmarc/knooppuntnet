package kpn.core.engine.analysis.route.segment

import kpn.core.engine.analysis.route.RouteAnalyzerFunctions
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

    val forwardPath: Option[Path] = findForwardPath().map(RouteAnalyzerFunctions.segmentToPath) // TODO paved/unpaved logic incomplete
    val backwardPath: Option[Path] = findBackwardPath().map(RouteAnalyzerFunctions.segmentToPath) // TODO paved/unpaved logic incomplete
    val usedSegments1 = forwardPath.toSeq.flatMap(_.segments) ++ backwardPath.toSeq.flatMap(_.segments)
    val usedFragments1 = usedSegments1.flatMap(_.segmentFragments).map(_.fragment).toSeq
    val availableFragments: Seq[Fragment] = (allFragments.toSet -- usedFragments1.toSet).toSeq
    val startTentaclePaths = new TentacleAnalyzer(segmentFinder, availableFragments, routeNodeAnalysis.startNodes.map(_.node)).findTentacles.map(RouteAnalyzerFunctions
      .segmentToPath) // TODO paved/unpaved logic incomplete
    val endTentaclePaths = new TentacleAnalyzer(segmentFinder, availableFragments, routeNodeAnalysis.endNodes.map(_.node)).findTentacles.map(RouteAnalyzerFunctions
      .segmentToPath) // TODO paved/unpaved logic incomplete
    val unusedPaths = {
      val used = forwardPath.toSeq.flatMap(_.segments) ++ backwardPath.toSeq.flatMap(_.segments) ++ startTentaclePaths.flatMap(_.segments) ++ endTentaclePaths.flatMap(_.segments)
      findUnusedSegments(used).map(RouteAnalyzerFunctions.segmentToPath) // TODO paved/unpaved logic incomplete
    }

    RouteStructure(
      forwardPath,
      backwardPath,
      startTentaclePaths,
      endTentaclePaths,
      unusedPaths
    )
  }

  case class PathNodes(sourceNode: RouteNode, targetNode: RouteNode)

  private def findForwardPath(): Option[Segment] = {
    findPath(routeNodeAnalysis.startNodes, routeNodeAnalysis.endNodes)
  }

  private def findBackwardPath(): Option[Segment] = {
    findPath(routeNodeAnalysis.endNodes, routeNodeAnalysis.startNodes)
  }

  private def findPath(sourceNodes: Seq[RouteNode], targetNodes: Seq[RouteNode]): Option[Segment] = {
    val startEndCombinations = sourceNodes.flatMap(sourceNode => targetNodes.map(targetNode => PathNodes(sourceNode, targetNode)))
    val segments = startEndCombinations.flatMap { path =>
      findSegment2(Forward, path.sourceNode.node, path.targetNode.node)
    }
    SegmentSelector.select(segments)
  }

  private def findSegment(direction: SegmentDirection.Value, startNode: Option[RouteNode], endNode: Option[RouteNode]): Option[Segment] = {
    if (startNode.isDefined && endNode.isDefined) {
      findSegment2(Forward, startNode.get.node, endNode.get.node)
    }
    else {
      None
    }
  }

  private def findTentacles(usedSegments: Iterable[Segment]): Seq[Segment] = {
    val usedFragments = usedSegments.flatMap(_.segmentFragments).map(_.fragment).toSeq
    val availableFragments: Seq[Fragment] = (allFragments.toSet -- usedFragments.toSet).toSeq
    val t1 = new TentacleAnalyzer(segmentFinder, availableFragments, routeNodeAnalysis.startNodes.map(_.node)).findTentacles
    val t2 = new TentacleAnalyzer(segmentFinder, availableFragments, routeNodeAnalysis.endNodes.map(_.node)).findTentacles
    t1 ++ t2
  }

  private def findSegment2(direction: SegmentDirection.Value, source: Node, target: Node): Option[Segment] = {
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
