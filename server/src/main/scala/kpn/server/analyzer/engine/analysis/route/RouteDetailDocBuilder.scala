package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.RouteSummary
import kpn.api.common.data.Element
import kpn.api.common.data.Way
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.custom.Fact
import kpn.api.custom.RouteMemberInfo
import kpn.api.custom.Timestamp
import kpn.core.analysis.RouteMemberWay
import kpn.core.doc.RouteDetailDoc
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class RouteDetailDocBuilder(context: RouteDetailAnalysisContext) {

  def build(): RouteDetailDoc = {

    val title: String = context.routeNameAnalysis.name match {
      case Some(routeName) => routeName
      case _ => "no-name"
    }

    val members: Seq[RouteMemberInfo] = context.routeMembers.map { member =>
      kpn.api.custom.RouteMemberInfo(
        member.id,
        member.memberType,
        member.memberType == "way",
        member.nodes,
        member.linkName,
        member.from: String,
        member.fromNode.id,
        member.to,
        member.toNode.id,
        member.role.getOrElse(""),
        member.element.timestamp,
        member.accessible,
        member.length,
        member.nodeCount,
        member.description,
        RouteAnalyzerFunctions.oneWay(member),
        RouteAnalyzerFunctions.oneWayTags(member)
      )
    }

    val length: Long = context.ways.map(_.length).sum

    val routeWays: Seq[Way] = {
      context.routeMembers.flatMap {
        case w: RouteMemberWay => Some(w.way)
        case _ => None
      }
    }

    def routeMemberWays: Seq[RouteMemberWay] = {
      context.routeMembers.flatMap {
        case w: RouteMemberWay => Some(w)
        case _ => None
      }
    }

    val accessible: Boolean = context.ways.size == routeMemberWays.count(_.accessible)

    val nameDerivedFromNodes = context.routeNameAnalysis.derivedFromNodes

    val routeAnalysis = RouteInfoAnalysis(
      context.unexpectedNodeIds.get,
      context.unexpectedRelationIds.get,
      members,
      context.expectedName.getOrElse(""),
      nameDerivedFromNodes,
      context.routeMap,
      new RouteStructureFormatter(context.structure).strings,
      context.geometryDigest,
      context.locationAnalysis.get
    )

    val lastUpdatedElement: Element = {
      val elements: Seq[Element] = Seq(context.relation) ++ routeWays ++ context.oldRouteNodeAnalysis.routeNodes.map(rn => rn.node)
      elements.reduceLeft((a, b) => if (a.timestamp > b.timestamp) a else b)
    }

    val lastUpdated: Timestamp = lastUpdatedElement.timestamp

    val nodeNames = routeAnalysis.map.freeNodes.map(_.name) ++
      routeAnalysis.map.startNodes.map(_.name) ++
      routeAnalysis.map.endNodes.map(_.name)

    val summary = RouteSummary(
      context.relation.id,
      context.country,
      context.scopedNetworkType.networkType,
      context.scopedNetworkType.networkScope,
      title,
      length,
      context.oldFacts.exists(_.isError),
      context.oldFacts.contains(Fact.RouteInaccessible),
      routeWays.size,
      context.relation.timestamp,
      nodeNames,
      context.relation.tags
    )

    RouteDetailDoc(
      summary.id,
      context.labels,
      summary,
      proposed = context.proposed,
      context.relation.version,
      context.relation.changeSetId,
      lastUpdated,
      context.lastSurvey,
      context.facts,
      context.oldFacts,
      routeAnalysis,
      context.tiles,
      routeAnalysis.map.nodeIds,
      context.elementIds,
      context.edges
    )
  }
}
