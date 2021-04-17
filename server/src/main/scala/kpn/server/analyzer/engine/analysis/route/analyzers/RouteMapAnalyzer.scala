package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.common.MapBounds
import kpn.api.common.common.TrackPath
import kpn.api.common.common.TrackPoint
import kpn.api.common.common.TrackSegment
import kpn.api.common.common.TrackSegmentFragment
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.route.RouteMap
import kpn.core.analysis.RouteMemberWay
import kpn.core.directions.DirectionAnalyzer
import kpn.core.directions.Latlon
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.analysis.route.RouteAnalyzerFunctions
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteStructure
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.analyzer.engine.analysis.route.segment.Segment

object RouteMapAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteMapAnalyzer(context).analyze
  }
}

class RouteMapAnalyzer(context: RouteAnalysisContext) {

  def analyze: RouteAnalysisContext = {

    val ways: Seq[Way] = context.routeMembers.get.flatMap {
      case w: RouteMemberWay => Some(w.way)
      case _ => None
    }

    val allWayNodes = ways.flatMap(w => w.nodes)
    val bounds = MapBounds(allWayNodes ++ context.routeNodeAnalysis.get.routeNodes.map(_.node))
    val routeMap = buildRouteMap(context.routeNodeAnalysis.get, context.structure.get, bounds)
    context.copy(ways = Some(ways), allWayNodes = Some(allWayNodes), routeMap = Some(routeMap))
  }

  private def buildRouteMap(routeNodeAnalysis: RouteNodeAnalysis, structure: RouteStructure, bounds: MapBounds): RouteMap = {
    val forwardBreakPoint = {
      structure.forwardPath match {
        case Some(path) if path.broken => Some(toTrackPoint(path.segments.last.nodes.last))
        case _ => None
      }
    }

    val backwardBreakPoint = {
      structure.backwardPath match {
        case Some(path) if path.broken => Some(toTrackPoint(path.segments.last.nodes.last))
        case _ => None
      }
    }

    val pathIdIterator = (1L to 10000L).iterator

    val forwardPathTrackPoints = structure.forwardPath.toSeq.flatMap(_.trackPoints)
    val backwardPathTrackPoints = structure.backwardPath.toSeq.flatMap(_.trackPoints)
    val same = forwardPathTrackPoints == backwardPathTrackPoints.reverse

    RouteMap(
      bounds,
      freePaths = structure.freePaths.map(path => toTrackPath(pathIdIterator, path)),
      forwardPath = structure.forwardPath.map(path => toTrackPath(pathIdIterator, path, oneWay = !same)),
      backwardPath = structure.backwardPath.map(path => toTrackPath(pathIdIterator, path, oneWay = !same)),
      unusedSegments = structure.unusedSegments.map(toTrackSegment),
      startTentaclePaths = structure.startTentaclePaths.map(path => toTrackPath(pathIdIterator, path)),
      endTentaclePaths = structure.endTentaclePaths.map(path => toTrackPath(pathIdIterator, path)),
      forwardBreakPoint = forwardBreakPoint,
      backwardBreakPoint = backwardBreakPoint,
      freeNodes = RouteAnalyzerFunctions.toInfos(routeNodeAnalysis.freeNodes),
      startNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.startNodes.isEmpty) Seq() else Seq(routeNodeAnalysis.startNodes.head)),
      endNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.endNodes.isEmpty) Seq() else Seq(routeNodeAnalysis.endNodes.head)),
      startTentacleNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.startNodes.size <= 1) Seq() else routeNodeAnalysis.startNodes.tail),
      endTentacleNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.endNodes.size <= 1) Seq() else routeNodeAnalysis.endNodes.tail),
      redundantNodes = RouteAnalyzerFunctions.toInfos(routeNodeAnalysis.redundantNodes),
      streets = context.streets.toSeq.flatten,
      trackPaths = structure.paths.toSeq.flatten.map(path => toTrackPath(pathIdIterator, path))
    )
  }

  private def toTrackPath(pathIdIterator: Iterator[Long], path: Path, oneWay: Boolean = false): TrackPath = {
    val trackSegments = path.segments.map(toTrackSegment)
    TrackPath(pathIdIterator.next(), path.startNodeId, path.endNodeId, path.meters, /*path.oneWay ||*/ oneWay, trackSegments)
  }

  private def toTrackSegment(segment: Segment): TrackSegment = {

    val fragments: Seq[TrackSegmentFragment] = segment.fragments.flatMap { fragment =>
      val streetIndex: Option[Long] = fragment.fragment.way.tags("name") match {
        case None => None
        case Some(street) =>
          context.streets match {
            case Some(streets) => streets.zipWithIndex.find(_._1 == street).map(_._2)
            case None => None
          }
      }

      if (fragment.nodes.size < 2) {
        Seq()
      }
      else {
        fragment.nodes.sliding(2).toSeq.map { case Seq(p1, p2) =>
          val meters = (Haversine.km(p1.lat, p1.lon, p2.lat, p2.lon) * 1000).toInt
          val orientation = DirectionAnalyzer.calculateHeading(Latlon(p1.lat, p1.lon), Latlon(p2.lat, p2.lon))
          TrackSegmentFragment(
            toTrackPoint(p2),
            meters,
            orientation,
            streetIndex
          )
        }
      }
    }

    val source: TrackPoint = toTrackPoint(segment.fragments.head.nodes.head)
    TrackSegment(segment.surface, source, fragments)
  }

  private def toTrackPoint(node: Node): TrackPoint = {
    TrackPoint(node.latitude, node.longitude)
  }

}
