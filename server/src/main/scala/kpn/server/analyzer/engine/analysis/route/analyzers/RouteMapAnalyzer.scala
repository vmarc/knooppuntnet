package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.core.analysis.RouteMemberWay
import kpn.core.directions.DirectionAnalyzer
import kpn.core.directions.Latlon
import kpn.server.analyzer.engine.analysis.route.RouteAnalyzerFunctions
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteStructure
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.segment.Path
import kpn.server.analyzer.engine.analysis.route.segment.Segment
import kpn.core.util.Haversine
import kpn.shared.common.MapBounds
import kpn.shared.common.TrackPath
import kpn.shared.common.TrackPoint
import kpn.shared.common.TrackSegment
import kpn.shared.common.TrackSegmentFragment
import kpn.shared.data.Node
import kpn.shared.data.Way
import kpn.shared.route.RouteMap

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

    RouteMap(
      bounds,
      forwardPath = structure.forwardPath.map(toTrackPath),
      backwardPath = structure.backwardPath.map(toTrackPath),
      unusedSegments = structure.unusedSegments.map(toTrackSegment),
      startTentaclePaths = structure.startTentaclePaths.map(toTrackPath),
      endTentaclePaths = structure.endTentaclePaths.map(toTrackPath),
      forwardBreakPoint = forwardBreakPoint,
      backwardBreakPoint = backwardBreakPoint,
      startNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.startNodes.isEmpty) Seq() else Seq(routeNodeAnalysis.startNodes.head)),
      endNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.endNodes.isEmpty) Seq() else Seq(routeNodeAnalysis.endNodes.head)),
      startTentacleNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.startNodes.size <= 1) Seq() else routeNodeAnalysis.startNodes.tail),
      endTentacleNodes = RouteAnalyzerFunctions.toInfos(if (routeNodeAnalysis.endNodes.size <= 1) Seq() else routeNodeAnalysis.endNodes.tail),
      redundantNodes = RouteAnalyzerFunctions.toInfos(routeNodeAnalysis.redundantNodes),
      streets = context.streets.toSeq.flatten
    )
  }

  private def toTrackPath(path: Path): TrackPath = {
    val trackSegments = path.segments.map(toTrackSegment)
    TrackPath(path.startNodeId, path.endNodeId, path.meters, trackSegments)
  }

  private def toTrackSegment(segment: Segment): TrackSegment = {

    val fragments: Seq[TrackSegmentFragment] = segment.fragments.flatMap { fragment =>
      val streetIndex: Option[Int] = fragment.fragment.way.tags("name") match {
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
    TrackPoint(node.latitude.toString, node.longitude.toString)
  }

}
