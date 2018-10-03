package kpn.core.engine.analysis.route

import kpn.core.analysis.NetworkNode
import kpn.core.analysis.RouteMember
import kpn.core.analysis.RouteMemberWay
import kpn.core.engine.analysis.Interpreter
import kpn.core.engine.analysis.OneWayAnalyzer
import kpn.core.engine.analysis.WayAnalyzer
import kpn.core.engine.analysis.route.analyzers.AccessibilityAnalyzer
import kpn.core.engine.analysis.route.analyzers.BigGermanBicycleRouteWithoutNameRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.BigGermanBicycleRouteWithoutNetworkNodesAnalyzer
import kpn.core.engine.analysis.route.analyzers.ExpectedNameRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.FixmeIncompleteRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.FixmeTodoRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.ForeignCountryRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.GermanHikingRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.IngoredRouteAnalysisBuilder
import kpn.core.engine.analysis.route.analyzers.NetworkTaggedAsRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.OverlappingWaysRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteAnalysisBuilder
import kpn.core.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteFragmentAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteMapAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteMemberAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteNameAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteNodeAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteStructureAnalyzer
import kpn.core.engine.analysis.route.analyzers.RouteTagRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.SuspiciousWaysRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.UnexpectedNodeRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.UnexpectedRelationRouteAnalyzer
import kpn.core.engine.analysis.route.analyzers.WithoutWaysRouteAnalyzer
import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.core.engine.analysis.route.segment.Segment
import kpn.core.load.data.LoadedRoute
import kpn.core.util.Log
import kpn.shared.common.TrackPoint
import kpn.shared.common.TrackSegment
import kpn.shared.data.Node
import kpn.shared.data.Tags
import kpn.shared.route.Both
import kpn.shared.route.RouteNetworkNodeInfo
import kpn.shared.route.WayDirection

import scala.annotation.tailrec

class MasterRouteAnalyzerImpl(accessibilityAnalyzer: AccessibilityAnalyzer) extends MasterRouteAnalyzer {

  private val log = Log(classOf[MasterRouteAnalyzerImpl])

  override def analyze(networkNodes: Map[Long, NetworkNode], loadedRoute: LoadedRoute, orphan: Boolean): RouteAnalysis = {
    Log.context("route=%07d".format(loadedRoute.id)) {

      val context = RouteAnalysisContext(
        networkNodes,
        loadedRoute,
        orphan,
        interpreter = new Interpreter(loadedRoute.networkType)
      )

      val analyzers: List[RouteAnalyzer] = List(
        ForeignCountryRouteAnalyzer,
        GermanHikingRouteAnalyzer,
        BigGermanBicycleRouteWithoutNameRouteAnalyzer,
        RouteTagRouteAnalyzer,
        WithoutWaysRouteAnalyzer,
        FixmeIncompleteRouteAnalyzer,
        FixmeTodoRouteAnalyzer,
        UnexpectedNodeRouteAnalyzer,
        UnexpectedRelationRouteAnalyzer,
        RouteNameAnalyzer,
        RouteNodeAnalyzer,
        BigGermanBicycleRouteWithoutNetworkNodesAnalyzer,
        NetworkTaggedAsRouteAnalyzer,
        ExpectedNameRouteAnalyzer,
        SuspiciousWaysRouteAnalyzer,
        OverlappingWaysRouteAnalyzer,
        RouteFragmentAnalyzer,
        RouteStructureAnalyzer,
        RouteMemberAnalyzer,
        RouteMapAnalyzer
      )

      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[RouteAnalyzer], context: RouteAnalysisContext): RouteAnalysis = {
    if (analyzers.isEmpty) {
      new RouteAnalysisBuilder(context).build
    }
    else {
      val newContext = analyzers.head.analyze(context)
      if (newContext.ignore) {
        new IngoredRouteAnalysisBuilder().build(newContext)
      }
      else {
        doAnalyze(analyzers.tail, newContext)
      }
    }
  }

}

object RouteAnalyzerFunctions {

  def toInfos(nodes: Seq[RouteNode]): Seq[RouteNetworkNodeInfo] = {
    nodes.map {
      routeNode =>
        RouteNetworkNodeInfo(
          routeNode.id,
          routeNode.name,
          routeNode.alternateName,
          routeNode.lat,
          routeNode.lon
        )
    }
  }

  def toTrackSegment(segment: Segment): TrackSegment = {
    val trackPoints = segment.segmentFragments.flatMap(_.nodes).map(toTrackPoint)
    TrackSegment(segment.surface, trackPoints)
  }

  def toTrackPoint(node: Node): TrackPoint = {
    TrackPoint(node.latitude.toString, node.longitude.toString)
  }


  def oneWay(member: RouteMember): WayDirection = {
    member match {
      case routeMemberWay: RouteMemberWay => new OneWayAnalyzer(routeMemberWay.way).direction
      case _ => Both
    }
  }

  def oneWayTags(member: RouteMember): Tags = {
    member match {
      case routeMemberWay: RouteMemberWay => OneWayAnalyzer.oneWayTags(routeMemberWay.way)
      case _ => Tags.empty
    }
  }
}
