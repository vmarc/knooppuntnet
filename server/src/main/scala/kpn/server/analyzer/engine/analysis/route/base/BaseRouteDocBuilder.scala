package kpn.server.analyzer.engine.analysis.route.base

import kpn.api.common.RouteMemberInfo
import kpn.api.common.data.Element
import kpn.api.common.data.MemberType
import kpn.api.common.data.Way
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.custom.Timestamp
import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteBaseData
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import org.springframework.stereotype.Component

@Component
class BaseRouteDocBuilder {

  def build(context: BaseRouteAnalysisContext): BaseRouteDoc = {

    val title: String = context.routeNameAnalysis.name match {
      case Some(routeName) => routeName
      case _ => "no-name"
    }

    val members: Seq[RouteMemberInfo] = context.routeMembers

    val length: Long = context.structure.allPaths.map(_.meters).sum

    val routeWays: Seq[Way] = context.relation.members.flatMap(_.way)

    val nameDerivedFromNodes = context.routeNameAnalysis.derivedFromNodes

    val routeAnalysis = RouteInfoAnalysis(
      context.expectedName.getOrElse("")
    )

    val lastUpdatedElement: Element = {
      val elements: Seq[Element] = Seq(context.relation) ++ routeWays ++ context.routeNodesAnalysis.nodes.map(rn => rn.node)
      elements.reduceLeft((a, b) => if (a.timestamp > b.timestamp) a else b)
    }

    val lastUpdated: Timestamp = lastUpdatedElement.timestamp

    val subRelationIds = context.routeMembers.flatMap { member =>
      member.memberType match {
        case MemberType.Relation => Some(member.id)
        case _ => None
      }
    }

    val nodeIds = context.routeNodesAnalysis.nodeIds
    val networkNodeIds = if (nodeIds.nonEmpty) {
      Some(nodeIds)
    }
    else {
      None
    }

    BaseRouteDoc(
      context.relation.id,
      active = true,
      base = RouteBaseData(
        raw = context.relation.raw,
        countries = context.countries,
        nodeNetwork = context.nodeNetwork,
        routeTypes = context.routeTypes,
        scopes = context.scopes,
        name = title,
        meters = length,
        wayCount = routeWays.size,
        proposed = context.proposed,
        lastUpdated = lastUpdated,
        lastSurvey = context.lastSurvey,
        unexpectedNodeIds = context.unexpectedNodeIds,
        members = members,
        nameDerivedFromNodes = nameDerivedFromNodes,
        nodes = context.routeNodesAnalysis.toRouteNodes,
        analysis = routeAnalysis,
        locationAnalysis = context._locationAnalysis.get,
        networkNodeIds = networkNodeIds,
        edges = context.edges,
      ),
      facts = context.facts,
      context.geometryDigest,
      context.elementIds,
      context.segments,
      context.segmentElements,
      context.paths,
      Some(context.relation),
      context.subRelationTree,
      subRelationIds,
      context.bounds,
    )
  }
}
