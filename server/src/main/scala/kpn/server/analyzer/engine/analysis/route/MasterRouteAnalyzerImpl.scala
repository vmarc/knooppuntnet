package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.route.Both
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.common.route.WayDirection
import kpn.api.custom.Relation
import kpn.api.custom.Tags
import kpn.core.analysis.RouteMember
import kpn.core.analysis.RouteMemberWay
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.analyzers.ExpectedNameRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.FactCombinationAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.FixmeTodoRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.GeometryDigestAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.IncompleteOkRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.IncompleteRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.ProposedAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalysisBuilder
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteFragmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLabelsAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLastSurveyAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteMapAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteMemberAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNameAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeTagAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteStreetsAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteStructureAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTagAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.SuspiciousWaysRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.UnexpectedNodeRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.UnexpectedRelationRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.WithoutWaysRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.AnalysisContext
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class MasterRouteAnalyzerImpl(
  analysisContext: AnalysisContext,
  routeCountryAnalyzer: RouteCountryAnalyzer,
  routeLocationAnalyzer: RouteLocationAnalyzer,
  routeTileAnalyzer: RouteTileAnalyzer
) extends MasterRouteAnalyzer {

  override def analyze(relation: Relation): Option[RouteAnalysis] = {
    Log.context("route=%07d".format(relation.id)) {

      val context = RouteAnalysisContext(analysisContext, relation)

      val analyzers: List[RouteAnalyzer] = List(
        routeCountryAnalyzer,
        RouteTagAnalyzer,
        ProposedAnalyzer,
        WithoutWaysRouteAnalyzer,
        IncompleteRouteAnalyzer,
        FixmeTodoRouteAnalyzer,
        UnexpectedNodeRouteAnalyzer,
        UnexpectedRelationRouteAnalyzer,
        RouteNameAnalyzer,
        RouteNodeTagAnalyzer,
        RouteNodeAnalyzer, // has ProposedAnalyzer as prerequisite
        ExpectedNameRouteAnalyzer, // <== needs further updating
        SuspiciousWaysRouteAnalyzer, // OK
        RouteFragmentAnalyzer,
        RouteStructureAnalyzer,
        RouteMemberAnalyzer,
        RouteStreetsAnalyzer,
        RouteMapAnalyzer,
        GeometryDigestAnalyzer,
        routeLocationAnalyzer,
        IncompleteOkRouteAnalyzer,
        FactCombinationAnalyzer,
        RouteLastSurveyAnalyzer,
        routeTileAnalyzer,
        RouteLabelsAnalyzer // this always should be the last analyzer
      )

      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[RouteAnalyzer], context: RouteAnalysisContext): Option[RouteAnalysis] = {
    if (context.abort) {
      None
    }
    else if (analyzers.isEmpty) {
      Some(new RouteAnalysisBuilder(context).build)
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}

object RouteAnalyzerFunctions {

  def toInfos(nodes: Seq[RouteNode]): Seq[RouteNetworkNodeInfo] = {
    nodes.map { routeNode =>
      RouteNetworkNodeInfo(
        routeNode.id,
        routeNode.name,
        routeNode.alternateName,
        routeNode.lat,
        routeNode.lon
      )
    }
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
