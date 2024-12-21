package kpn.server.analyzer.engine.analysis.route.analyzers.route

import kpn.api.common.data.MemberType
import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment
import kpn.api.common.route.RouteStructureRelation
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.RouteStructureWay
import kpn.api.custom.RouteMemberInfo
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteDetailRepository
import org.springframework.stereotype.Component

@Component
class RouteStructureRowsAnalyzer(routeDetailRepository: RouteDetailRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val rows = context.routeDetailDoc.members.flatMap { member =>
      if (member.memberType == MemberType.Relation) {
        relationRows(1, member, Seq.empty)
      }
      else if (member.memberType == MemberType.Way) {
        Seq(wayRow(member))
      }
      else {
        Seq(nodeRow(member))
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
      linkName = "n",
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
      linkName = member.linkName,
      distance = member.distance,
      way = Some(
        RouteStructureWay(
          nodes = member.nodes,
          from = member.from,
          fromNodeId = member.fromNodeId,
          to = member.to,
          toNodeId = member.toNodeId,
          accessible = member.accessible,
          nodeCount = member.nodeCount,
          description = member.description,
          oneWay = member.oneWay,
          oneWayTags = member.oneWayTags
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
      routeDetailRepository.findById(member.id) match {
        case None => Seq.empty
        case Some(routeDetailDoc) =>
          val subRelationMembers = routeDetailDoc.members.filter(_.memberType == MemberType.Relation)
          val subRows: Seq[RouteStructureRow] = subRelationMembers.flatMap(subRelationMember =>
            relationRows(level + 1, subRelationMember, processedRelationIds :+ member.id)
          )
          val distance = routeDetailDoc.summary.meters
          val subRowsDistance = subRows.flatMap(_.relation.map(_.totalDistance)).sum
          val totalDistance = distance + subRowsDistance

          RouteStructureRow(
            id = member.id,
            memberType = member.memberType,
            role = member.role,
            linkName = "r",
            distance = distance,
            way = None,
            relation = Some(
              RouteStructureRelation(
                level = level,
                physical = false,
                name = routeDetailDoc.summary.name,
                subRelationIndex = None,
                // role: Option[String],
                survey = None,
                symbol = None,
                osmSegmentCount = Some(routeDetailDoc.segments.size),
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
