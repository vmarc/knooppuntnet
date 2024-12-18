package kpn.server.analyzer.engine.analysis.route.analyzers.route

import kpn.api.common.data.MemberType
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.WayDirection
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
      else {
        Seq(wayNodeRow(member))
      }
    }

    context.copy(_structureRows = Some(rows))
  }

  private def wayNodeRow(member: RouteMemberInfo): RouteStructureRow = {
    RouteStructureRow(
      // RouteMemberInfo
      member.id,
      member.memberType,
      member.isWay,
      member.nodes,
      member.linkName,
      member.from,
      member.fromNodeId,
      member.to,
      member.toNodeId,
      member.role,
      member.timestamp,
      member.accessible,
      member.length,
      member.nodeCount,
      member.description,
      member.oneWay,
      member.oneWayTags,

      level = 0, // Long
      physical = false, // Boolean
      name = "", // String
      relationId = member.id,
      subRelationIndex = None, // Option[Long]
      survey = None, // Option[Day]
      symbol = None, // Option[String]
      osmSegmentCount = None, // Option[Long]
      osmDistance = 0, // Long
      gaps = None, // Option[String]
      happy = false
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
          val subRows: Seq[RouteStructureRow] = subRelationMembers.flatMap(member => relationRows(level + 1, member, processedRelationIds :+ member.id))
          RouteStructureRow(
            // RouteMemberInfo
            id = member.id,
            memberType = member.memberType,
            isWay = false,
            nodes = Seq.empty,
            linkName = "",
            from = "",
            fromNodeId = 0,
            to = "",
            toNodeId = 0,
            role = member.role,
            timestamp = null,
            accessible = false,
            length = "",
            nodeCount = "",
            description = "",
            oneWay = WayDirection.Both,
            oneWayTags = Seq.empty,

            // MonitorRouteRelationStructureRow
            level = level,
            physical = false,
            name = routeDetailDoc.summary.name,
            relationId = member.id,
            subRelationIndex = None,
            // role: Option[String],
            survey = None,
            symbol = None,
            osmSegmentCount = Some(routeDetailDoc.segments.size),
            osmDistance = routeDetailDoc.summary.meters,
            gaps = None,
            happy = false,
          ) +: subRows
      }
    }
  }
}
