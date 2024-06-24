package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.RouteSummary
import kpn.api.common.data.Element
import kpn.api.common.data.Way
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.common.route.RouteMap
import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.RouteMemberInfo
import kpn.api.custom.Timestamp
import kpn.core.analysis.RouteMember
import kpn.core.analysis.RouteMemberWay
import kpn.core.doc.RouteDoc
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteAnalyzerFunctions
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteStructure
import kpn.server.analyzer.engine.analysis.route.RouteStructureFormatter
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

import scala.collection.mutable.ListBuffer

class RouteAnalysisBuilder(context: RouteAnalysisContext) {

  val facts: ListBuffer[Fact] = ListBuffer[Fact]()
  facts ++= context.facts
  if (facts.exists(_.isError)) {
    facts += RouteBroken
  }

  val oldFacts: ListBuffer[Fact] = ListBuffer[Fact]()
  oldFacts ++= context.oldFacts
  if (oldFacts.exists(_.isError)) {
    oldFacts += RouteBroken
  }

  def build: RouteAnalysis = {

    val title: String = context.routeNameAnalysis.name match {
      case Some(routeName) => routeName
      case _ => "no-name"
    }

    val route = buildRoute(
      title,
      context.routeMembers.get,
      context.ways.get,
      context.routeMap,
      context.unexpectedNodeIds.get,
      context.unexpectedRelationIds.get,
      context.expectedName.getOrElse(""),
      context.structure,
      context.routeNodeAnalysis
    )

    RouteAnalysis(
      context.relation,
      route = route,
      structure = context.structure,
      routeNodeAnalysis = context.routeNodeAnalysis,
      routeMembers = context.routeMembers.get,
      ways = context.ways.get,
      startNodes = context.routeMap.startNodes,
      endNodes = context.routeMap.endNodes,
      startTentacleNodes = context.routeMap.startTentacleNodes,
      endTentacleNodes = context.routeMap.endTentacleNodes,
      allWayNodes = context.allWayNodes.get,
      bounds = context.routeMap.bounds,
      geometryDigest = context.geometryDigest.get,
      tileAnalysis = context.tileAnalysis.get
    )
  }

  private def buildRoute(
    title: String,
    routeMembers: Seq[RouteMember],
    ways: Seq[Way],
    routeMap: RouteMap,
    unexpectedNodeIds: Seq[Long],
    unexpectedRelationIds: Seq[Long],
    expectedName: String,
    structure: RouteStructure,
    routeNodeAnalysis: RouteNodeAnalysis
  ): RouteDoc = {

    val members: Seq[RouteMemberInfo] = routeMembers.map { member =>

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

    val length: Long = ways.map(_.length).sum

    val routeWays: Seq[Way] = {
      routeMembers.flatMap {
        case w: RouteMemberWay => Some(w.way)
        case _ => None
      }
    }

    def routeMemberWays: Seq[RouteMemberWay] = {
      routeMembers.flatMap {
        case w: RouteMemberWay => Some(w)
        case _ => None
      }
    }

    val accessible: Boolean = ways.size == routeMemberWays.count(_.accessible)

    val nameDerivedFromNodes = context.routeNameAnalysis.derivedFromNodes

    val routeAnalysis = RouteInfoAnalysis(
      unexpectedNodeIds,
      unexpectedRelationIds,
      members,
      expectedName,
      nameDerivedFromNodes,
      routeMap,
      new RouteStructureFormatter(structure).strings,
      context.geometryDigest.get,
      context.locationAnalysis.get
    )

    val lastUpdatedElement: Element = {
      val elements: Seq[Element] = Seq(context.relation) ++ routeWays ++ routeNodeAnalysis.routeNodes.map(rn => rn.node)
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
      facts.exists(_.isError),
      facts.contains(Fact.RouteInaccessible),
      routeWays.size,
      context.relation.timestamp,
      nodeNames,
      context.relation.tags
    )

    RouteDoc(
      summary.id,
      context.labels,
      summary,
      proposed = context.proposed,
      context.relation.version,
      context.relation.changeSetId,
      lastUpdated,
      context.lastSurvey,
      context.relation.tags,
      facts.toSeq,
      oldFacts.toSeq,
      routeAnalysis,
      context.tiles,
      routeAnalysis.map.nodeIds,
      context.elementIds,
      context.edges
    )
  }
}
