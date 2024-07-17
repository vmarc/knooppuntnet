package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.Bounds
import kpn.api.common.RouteSummary
import kpn.api.common.data.Element
import kpn.api.common.data.Node
import kpn.api.common.data.Way
import kpn.api.common.route.RouteInfoAnalysis
import kpn.api.custom.Fact
import kpn.api.custom.RouteMemberInfo
import kpn.api.custom.Timestamp
import kpn.core.analysis.RouteMemberWay
import kpn.core.doc.RouteDetailDoc
import kpn.core.doc.RouteDetailPath
import kpn.core.doc.RouteDetailSegment
import kpn.core.doc.RouteDetailSegmentElement
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.StructurePath

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

    val length: Long = context.relation.wayMembers.map(_.way.length).sum

    val routeWays: Seq[Way] = context.relation.wayMembers.map(_.way)

    def routeMemberWays: Seq[RouteMemberWay] = {
      context.routeMembers.flatMap {
        case w: RouteMemberWay => Some(w)
        case _ => None
      }
    }

    val accessible: Boolean = context.ways.size == routeMemberWays.count(_.accessible)

    val nameDerivedFromNodes = context.routeNameAnalysis.derivedFromNodes

    val routeAnalysis = RouteInfoAnalysis(
      context.expectedName.getOrElse(""),
      context.routeMap,
      new RouteStructureFormatter(context.oldStructure).strings,
    )

    val lastUpdatedElement: Element = {
      val elements: Seq[Element] = Seq(context.relation) ++ routeWays ++ context.nodes.nodes.map(rn => rn.node)
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
      context.unexpectedNodeIds,
      context.unexpectedRelationIds,
      members,
      nameDerivedFromNodes,
      context.nodes.toRouteNodes,
      routeAnalysis,
      context.geometryDigest,
      context._locationAnalysis.get,
      context.tiles,
      routeAnalysis.map.nodeIds,
      context.elementIds,
      context.edges,
      buildSegments,
      buildSegmentElements,
      buildPaths
    )
  }

  private def buildSegments: Seq[RouteDetailSegment] = {
    val ways = context.relation.wayMembers.map(_.way)
    context.segments.map { segment =>
      val segmentWayIds = segment.elements.flatMap(_.fragments).map(_.wayId)
      val segmentWays = segmentWayIds.flatMap(wayId => ways.find(_.id == wayId))
      val meters = segmentWays.map(_.length).sum
      val segmentNodes = segmentWays.flatMap(_.nodes)
      val bounds = Bounds.from(segmentNodes)
      RouteDetailSegment(
        segment.id,
        segment.fromNodeId,
        segment.toNodeId,
        meters,
        bounds,
        segment.elements.map(_.id)
      )
    }
  }

  private def buildSegmentElements: Seq[RouteDetailSegmentElement] = {

    val nodeMap: Map[Long, Node] = {
      val nodeMemberNodes = context.relation.nodeMembers.map(_.node).toSet
      val wayMemberNodes = context.relation.wayMembers.flatMap(_.way.nodes).toSet
      val nodes = nodeMemberNodes ++ wayMemberNodes
      nodes.map(node => node.id -> node)
    }.toMap

    context.segments.flatMap { segment =>
      segment.elements.flatMap { element =>
        element.fragmentGroups.map { fragmentGroup =>
          val nodes = fragmentGroup.nodeIds.flatMap(nodeId => nodeMap.get(nodeId))
          val coordinates = nodes.map(node => s"[${node.longitude},${node.latitude}]").mkString("[", ",", "]")
          RouteDetailSegmentElement(
            segment.id,
            element.id,
            fragmentGroup.surface,
            coordinates
          )
        }
      }
    }
  }

  private def buildPaths: Seq[RouteDetailPath] = {
    Seq(
      context.structure.forwardPath.toSeq.map(path => toRouteDetailPath(path, "forward")),
      context.structure.backwardPath.toSeq.map(path => toRouteDetailPath(path, "backward")),
      context.structure.startTentaclePaths.zipWithIndex.map { case (path, index) => toRouteDetailPath(path, s"start-tentacle-${index + 1}") },
      context.structure.endTentaclePaths.zipWithIndex.map { case (path, index) => toRouteDetailPath(path, s"end-tentacle-${index + 1}") },
      context.structure.otherPaths.zipWithIndex.map { case (path, index) => toRouteDetailPath(path, s"other-${index + 1}") },
    ).flatten
  }

  private def toRouteDetailPath(path: StructurePath, name: String): RouteDetailPath = {
    RouteDetailPath(path.id, name, path.elementIds)
  }
}
