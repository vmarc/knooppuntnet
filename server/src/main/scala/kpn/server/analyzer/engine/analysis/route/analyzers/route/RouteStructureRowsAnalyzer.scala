package kpn.server.analyzer.engine.analysis.route.analyzers.route

import kpn.api.common.route.RouteNetworkNodeInfo
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.WayDirection
import kpn.api.custom.RouteMemberInfo
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteDetailRepository
import org.springframework.stereotype.Component

@Component
class RouteStructureRowsAnalyzer(routeDetailRepository: RouteDetailRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val rows = context.routeDetailDoc.members.flatMap { member =>
      if (member.memberType == "relation") {
        relationRows(1, member)
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
      member.id: Long,
      member.memberType: String,
      member.isWay: Boolean,
      member.nodes: Seq[RouteNetworkNodeInfo],
      member.linkName: String,
      member.from: String,
      member.fromNodeId: Long,
      member.to: String,
      member.toNodeId: Long,
      member.role: String,
      member.timestamp: Timestamp,
      member.accessible: Boolean,
      member.length: String,
      member.nodeCount: String,
      member.description: String,
      member.oneWay: WayDirection,
      member.oneWayTags: Seq[Tag],

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

  private def relationRows(level: Int, member: RouteMemberInfo): Seq[RouteStructureRow] = {

    routeDetailRepository.findById(member.id) match {
      case None => Seq.empty
      case Some(routeDetailDoc) =>
        val subRelationMembers = routeDetailDoc.members.filter(_.memberType == "relation")
        val subRows: Seq[RouteStructureRow] = subRelationMembers.flatMap(member => relationRows(level + 1, member))

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
