package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.route.Both
import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.common.route.WayDirection
import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Relation
import kpn.api.custom.Tag
import kpn.core.analysis.RouteMember
import kpn.core.analysis.RouteMemberWay
import kpn.core.tools.next.domain.RouteRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.analyzers.EdgeRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.ExpectedNameRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.FactCombinationAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.FixmeTodoRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.GeometryDigestAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.IncompleteOkRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.IncompleteRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.OldRouteNodeTagAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.OldRouteStructureAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.OldRouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.ProposedAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteContextAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteElementsAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteFragmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLabelsAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLastSurveyAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteLocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteMapAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteMemberAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNameAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNameFromNodesAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNetworkTypeAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteNodeAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteStreetsAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteStructureAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTagAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.SuspiciousWaysRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.UnexpectedNodeRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.UnexpectedRelationRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.analyzers.WithoutWaysRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.RouteLinkAnalyzer
import kpn.server.analyzer.engine.analysis.route.structure.RouteSegmentAnalyzer
import org.springframework.stereotype.Component

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

@Component
class RouteDetailMainAnalyzer(
  routeCountryAnalyzer: RouteCountryAnalyzer,
  routeLocationAnalyzer: RouteLocationAnalyzer,
  oldRouteTileAnalyzer: OldRouteTileAnalyzer,
  routeTileAnalyzer: RouteTileAnalyzer
) {

  def analyze(
    relation: Relation,
    hierarchy: Option[RouteRelation],
    traceEnabled: Boolean = false
  ): Option[RouteDetailAnalysisContext] = {

    Log.context("route=%07d".format(relation.id)) {

      val context = RouteDetailAnalysisContext(relation, hierarchy, traceEnabled = traceEnabled)

      val analyzers: List[RouteAnalyzer] = List(
        RouteTagAnalyzer,
        RouteNetworkTypeAnalyzer,
        routeCountryAnalyzer, // TODO redesign - support multiple countries?
        ProposedAnalyzer,
        WithoutWaysRouteAnalyzer,
        IncompleteRouteAnalyzer,
        FixmeTodoRouteAnalyzer,
        UnexpectedNodeRouteAnalyzer,
        UnexpectedRelationRouteAnalyzer, // TODO redesign - move to pass 2?
        OldRouteNodeTagAnalyzer,
        RouteNameAnalyzer,

        //OldRouteNodeAnalyzer,
        RouteNameFromNodesAnalyzer,
        ExpectedNameRouteAnalyzer, // <== needs further updating
        SuspiciousWaysRouteAnalyzer, // OK

        RouteNodeAnalyzer,
        RouteLinkAnalyzer,
        RouteSegmentAnalyzer,
        RouteStructureAnalyzer,

        RouteFragmentAnalyzer,
        OldRouteStructureAnalyzer,

        RouteMemberAnalyzer,
        RouteStreetsAnalyzer,
        RouteMapAnalyzer,
        GeometryDigestAnalyzer,
        routeLocationAnalyzer,
        IncompleteOkRouteAnalyzer,
        FactCombinationAnalyzer,
        RouteLastSurveyAnalyzer,
        RouteElementsAnalyzer,
        oldRouteTileAnalyzer,
        routeTileAnalyzer,
        EdgeRouteAnalyzer,
        RouteLabelsAnalyzer, // this always should be the last analyzer
        RouteContextAnalyzer // helper to be used during development only
      )

      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[RouteAnalyzer], context: RouteDetailAnalysisContext): Option[RouteDetailAnalysisContext] = {
    if (context.abort) {
      None
    }
    else if (analyzers.isEmpty) {

      val facts: ListBuffer[Fact] = ListBuffer[Fact]()
      facts ++= context.facts
      if (facts.exists(_.isError)) {
        if (!facts.contains(RouteBroken)) {
          facts += RouteBroken
        }
      }

      val oldFacts: ListBuffer[Fact] = ListBuffer[Fact]()
      oldFacts ++= context.oldFacts
      if (oldFacts.exists(_.isError)) {
        oldFacts += RouteBroken
        if (!oldFacts.contains(RouteBroken)) {
          oldFacts += RouteBroken
        }
      }

      Some(
        context.copy(
          facts = facts.toSeq,
          oldFacts = oldFacts.toSeq,
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

  def toInfos(nodes: Seq[RouteNode]): Seq[RouteNetworkNodeInfo] = {
    nodes.map { routeNode =>
      RouteNetworkNodeInfo(
        routeNode.id,
        routeNode.name,
        routeNode.alternateName,
        routeNode.longName,
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

  def oneWayTags(member: RouteMember): Seq[Tag] = {
    member match {
      case routeMemberWay: RouteMemberWay => OneWayAnalyzer.oneWayTags(routeMemberWay.way)
      case _ => Seq.empty
    }
  }
}
