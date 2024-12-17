package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.Fact
import kpn.api.common.Fact.RouteBroken
import kpn.api.common.route.WayDirection
import kpn.api.custom.Relation
import kpn.api.custom.Tag
import kpn.core.analysis.Facts
import kpn.core.analysis.RouteMember
import kpn.core.analysis.RouteMemberWay
import kpn.core.doc.RouteRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.ExpectedNameRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.FactCombinationAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.FixmeTodoRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.GeometryDigestAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.IncompleteOkRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.IncompleteRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.ProposedAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteContextAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteDetailAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteEdgeAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteElementsAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteLabelsAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteLastSurveyAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteLinkAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteLocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteMemberAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteNameAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteNetworkTypeAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteNodesAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteOneWayAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteScopeAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteSegmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteSegmentAnalyzer2
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteStructureAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteTagAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.RouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.SuspiciousWaysRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.UnexpectedNodeRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.UnexpectedRelationRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.detail.WithoutWaysRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import org.springframework.stereotype.Component

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

@Component
class RouteDetailMainAnalyzer(
  routeCountryAnalyzer: RouteCountryAnalyzer,
  routeLocationAnalyzer: RouteLocationAnalyzer,
  routeTileAnalyzer: RouteTileAnalyzer
) {

  def analyze(
    relation: Relation,
    hierarchy: Option[RouteRelation],
    traceEnabled: Boolean = false
  ): Option[RouteDetailAnalysisContext] = {

    Log.context(f"route=${relation.id}%07d") {

      val context = RouteDetailAnalysisContext(relation, hierarchy, traceEnabled = traceEnabled)

      val analyzers: List[RouteDetailAnalyzer] = List(
        RouteTagAnalyzer,
        RouteNetworkTypeAnalyzer,
        RouteScopeAnalyzer,
        routeCountryAnalyzer, // TODO redesign - support multiple countries?
        ProposedAnalyzer,
        WithoutWaysRouteAnalyzer,
        IncompleteRouteAnalyzer,
        FixmeTodoRouteAnalyzer,
        UnexpectedNodeRouteAnalyzer,
        UnexpectedRelationRouteAnalyzer, // TODO redesign - move to pass 2?
        RouteNameAnalyzer,

        //OldRouteNodeAnalyzer,
        // TODO RouteNameFromNodesAnalyzer,
        SuspiciousWaysRouteAnalyzer, // OK

        RouteLinkAnalyzer,
        RouteNodesAnalyzer,
        ExpectedNameRouteAnalyzer,
        RouteSegmentAnalyzer,
        RouteOneWayAnalyzer,
        RouteStructureAnalyzer,
        RouteSegmentAnalyzer2,

        RouteMemberAnalyzer,
        GeometryDigestAnalyzer,
        routeLocationAnalyzer,
        IncompleteOkRouteAnalyzer,
        FactCombinationAnalyzer,
        RouteLastSurveyAnalyzer,
        RouteElementsAnalyzer,
        routeTileAnalyzer,
        RouteEdgeAnalyzer,
        RouteLabelsAnalyzer, // this always should be the last analyzer
        RouteContextAnalyzer // helper to be used during development only
      )

      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[RouteDetailAnalyzer], context: RouteDetailAnalysisContext): Option[RouteDetailAnalysisContext] = {
    if (context.abort) {
      None
    }
    else if (analyzers.isEmpty) {

      val facts: ListBuffer[Fact] = ListBuffer[Fact]()
      facts ++= context.facts
      if (facts.exists(Facts.isError)) {
        if (!facts.contains(RouteBroken)) {
          facts += RouteBroken
        }
      }

      Some(
        context.copy(
          facts = facts.toSeq,
        )
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}

object RouteAnalyzerFunctions {

  def oneWay(member: RouteMember): WayDirection = {
    member match {
      case routeMemberWay: RouteMemberWay => new OneWayAnalyzer(routeMemberWay.way).direction
      case _ => WayDirection.Both
    }
  }

  def oneWayTags(member: RouteMember): Seq[Tag] = {
    member match {
      case routeMemberWay: RouteMemberWay => OneWayAnalyzer.oneWayTags(routeMemberWay.way)
      case _ => Seq.empty
    }
  }
}
