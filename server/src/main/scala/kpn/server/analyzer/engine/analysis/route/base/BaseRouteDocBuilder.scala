package kpn.server.analyzer.engine.analysis.route.base

import kpn.api.common.Fact
import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteSummary
import kpn.api.common.data.Element
import kpn.api.common.data.MemberType.Relation
import kpn.api.common.data.Way
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.custom.Timestamp
import kpn.core.analysis.Facts
import kpn.core.doc.BaseRouteDoc
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext

class BaseRouteDocBuilder(context: BaseRouteAnalysisContext) {

  def build(): BaseRouteDoc = {

    val title: String = context.routeNameAnalysis.name match {
      case Some(routeName) => routeName
      case _ => "no-name"
    }

    val members: Seq[RouteMemberInfo] = context.routeMembers

    val length: Long = context.structure.allPaths.map(_.meters).sum

    val routeWays: Seq[Way] = context.relation.wayMembers.map(_.way)

    val nameDerivedFromNodes = context.routeNameAnalysis.derivedFromNodes

    val routeAnalysis = RouteInfoAnalysis(
      context.expectedName.getOrElse("")
    )

    val lastUpdatedElement: Element = {
      val elements: Seq[Element] = Seq(context.relation) ++ routeWays ++ context.routeNodesAnalysis.nodes.map(rn => rn.node)
      elements.reduceLeft((a, b) => if (a.timestamp > b.timestamp) a else b)
    }

    val lastUpdated: Timestamp = lastUpdatedElement.timestamp

    val summary = RouteSummary(
      context.relation.id,
      context.countries,
      context.nodeNetwork,
      context.routeTypes,
      context.scopes,
      title,
      length,
      context.facts.exists(Facts.isError),
      context.facts.contains(Fact.RouteInaccessible),
      routeWays.size,
      context.relation.timestamp,
      context.relation.tags
    )

    val subRelationIds = context.routeMembers.flatMap { member =>
      // TODO redesign - exclude subrelations that are not routes
      member.memberType match {
        case Relation => Some(member.id)
        case _ => None
      }
    }

    BaseRouteDoc(
      summary.id,
      context.labels,
      summary,
      proposed = context.proposed,
      context.relation.version,
      context.relation.changeSetId,
      lastUpdated,
      context.lastSurvey,
      context.facts,
      context.unexpectedNodeIds,
      context.unexpectedRelationIds,
      members,
      nameDerivedFromNodes,
      context.routeNodesAnalysis.toRouteNodes,
      routeAnalysis,
      context.geometryDigest,
      context._locationAnalysis.get,
      context.tiles,
      context.routeNodesAnalysis.nodeIds,
      context.elementIds,
      context.edges,
      context.segments,
      context.segmentElements,
      context.paths,
      context.hierarchy,
      context.bounds,
      subRelationIds
    )
  }
}
