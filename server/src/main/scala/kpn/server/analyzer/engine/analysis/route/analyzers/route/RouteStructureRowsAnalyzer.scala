package kpn.server.analyzer.engine.analysis.route.analyzers.route

import kpn.api.common.RouteMemberInfo
import kpn.api.common.data.MemberType
import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment
import kpn.api.common.route.RouteStructureRelation
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.RouteStructureWay
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteDetailRepository
import org.springframework.stereotype.Component

@Component
class RouteStructureRowsAnalyzer(routeDetailRepository: RouteDetailRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val rows = context.routeDetailDoc.members.flatMap { member =>
      member.memberType match {
        case MemberType.Relation => relationRows(1, member, Seq.empty)
        case MemberType.Way => Seq(wayRow(member))
        case MemberType.Node => Seq(nodeRow(member))
      }
    }

    val segments = context.routeDetailDoc.segments.map { segment =>
      RouteSegment(
        segment.id,
        segment.startNodeId,
        segment.endNodeId,
        segment.meters,
        segment.bounds,
        segment.elementIds
      )
    }

    val paths = context.routeDetailDoc.paths.map { path =>
      RoutePath(
        path.id,
        path.name,
        path.elementIds
      )
    }

    val distance = rows.map(_.distance).sum
    context.copy(
      _structureRows = Some(rows),
      _segments = Some(segments),
      _paths = Some(paths),
      _distance = Some(distance)
    )
  }

  private def nodeRow(member: RouteMemberInfo): RouteStructureRow = {
    RouteStructureRow(
      member.id,
      member.memberType,
      member.role,
      link = None,
      distance = 0,
      None,
      None,
    )
  }

  private def wayRow(member: RouteMemberInfo): RouteStructureRow = {
    RouteStructureRow(
      member.id,
      member.memberType,
      member.role,
      link = member.way.map(_.link),
      distance = member.way.map(_.distance).sum,
      way = member.way.map(way =>
        RouteStructureWay(
          nodes = way.nodes,
          from = way.from,
          fromNodeId = way.fromNodeId,
          to = way.to,
          toNodeId = way.toNodeId,
          accessible = way.accessible,
          nodeCount = way.nodeCount,
          description = way.description,
          oneWay = way.oneWay,
          oneWayTags = way.oneWayTags
        )
      ),
      None,
    )
  }

  private def relationRows(
    level: Int,
    member: RouteMemberInfo,
    processedRelationIds: Seq[Long]
  ): Seq[RouteStructureRow] = {

    if (processedRelationIds.contains(member.id)) {
      Seq.empty
    }
    else {
      routeDetailRepository.subRouteData(member.id) match {
        case None => Seq.empty
        case Some(subRouteData) =>
          val subRelationMembers = subRouteData.members.filter(_.memberType == MemberType.Relation)
          val subRows: Seq[RouteStructureRow] = subRelationMembers.flatMap(subRelationMember =>
            relationRows(level + 1, subRelationMember, processedRelationIds :+ member.id)
          )
          val distance = subRouteData.distance
          val subRowsDistance = subRows.flatMap(_.relation.map(_.totalDistance)).sum
          val totalDistance = distance + subRowsDistance

          RouteStructureRow(
            id = member.id,
            memberType = member.memberType,
            role = member.role,
            link = None,
            distance = distance,
            way = None,
            relation = Some(
              RouteStructureRelation(
                level = level,
                physical = false,
                name = subRouteData.name,
                subRelationIndex = None,
                survey = None,
                symbol = None,
                osmSegmentCount = None, //Some(subRouteData.segments.size),
                totalDistance = totalDistance,
                gaps = None,
                happy = false,
              )
            )
          ) +: subRows
      }
    }
  }
}
