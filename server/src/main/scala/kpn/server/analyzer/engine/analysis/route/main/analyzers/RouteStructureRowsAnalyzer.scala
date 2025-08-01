package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.api.common.RouteMemberInfo
import kpn.api.common.RouteMemberInfoWay
import kpn.api.common.data.MemberType
import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment
import kpn.api.common.route.RouteStructureRelation
import kpn.api.common.route.RouteStructureRow
import kpn.api.common.route.RouteStructureWay
import kpn.core.doc.SubRouteData
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteStructureRowsAnalyzer(routeRepository: RouteRepository) extends RouteAnalyzer {
  override def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    val rows = buildRows(context)
    val segments = buildSegments(context)
    val paths = buildPaths(context)
    val distance = calculateDistance(rows)
    context.copy(
      _structureRows = Some(rows),
      _segments = Some(segments),
      _paths = Some(paths),
      _distance = Some(distance)
    )
  }

  private def buildRows(context: RouteAnalysisContext): Seq[RouteStructureRow] = {
    val level = 1L
    context.route.members.zipWithIndex.flatMap { case (member, index) =>
      member.memberType match {
        case MemberType.Relation => buildRelationRows(level, Seq(index + 1), member, Seq.empty)
        case MemberType.Way => Seq(buildWayRow(level, member, index + 1))
        case MemberType.Node => Seq(buildNodeRow(level, member, index + 1))
      }
    }
  }

  private def buildSegments(context: RouteAnalysisContext): Seq[RouteSegment] = {
    context.route.segments.map { segment =>
      RouteSegment(
        segment.id,
        segment.startNodeId,
        segment.endNodeId,
        segment.meters,
        segment.bounds,
        segment.elementIds
      )
    }
  }

  private def buildPaths(context: RouteAnalysisContext): Seq[RoutePath] = {
    context.route.paths.map { path =>
      RoutePath(
        path.id,
        path.name,
        path.elementIds
      )
    }
  }

  private def calculateDistance(rows: Seq[RouteStructureRow]): Long = {
    rows.map(_.distance).sum
  }

  private def buildNodeRow(level: Long, member: RouteMemberInfo, rowNumber: Int): RouteStructureRow = {
    RouteStructureRow(
      s"$rowNumber",
      level,
      member.id,
      member.memberType,
      member.role,
      link = None,
      distance = 0,
      member.name,
      member.poi,
      None,
      None,
    )
  }

  private def buildWayRow(level: Long, member: RouteMemberInfo, rowNumber: Int): RouteStructureRow = {
    RouteStructureRow(
      s"$rowNumber",
      level,
      member.id,
      member.memberType,
      member.role,
      link = member.way.map(_.link),
      distance = member.way.map(_.distance).sum,
      name = member.name,
      poi = member.poi,
      way = member.way.map(toRouteStructureWay),
      None,
      segmentIds = member.segmentIds,
      pathIds = member.pathIds,
    )
  }

  private def toRouteStructureWay(way: RouteMemberInfoWay): RouteStructureWay = {
    RouteStructureWay(
      wayType = way.wayType,
      nodes = way.nodes,
      surface = way.surface,
      accessible = way.accessible,
      nodeCount = way.nodeCount,
      oneWay = way.oneWay,
      oneWayTags = way.oneWayTags
    )
  }

  private def buildRelationRows(
    level: Long,
    rowNumbers: Seq[Int],
    member: RouteMemberInfo,
    processedRelationIds: Seq[Long]
  ): Seq[RouteStructureRow] = {

    if (processedRelationIds.contains(member.id)) {
      return Seq.empty
    }

    routeRepository.subRouteData(member.id) match {
      case None => Seq.empty
      case Some(subRouteData) =>
        val subRows = buildSubRelationRows(level, rowNumbers, member, processedRelationIds, subRouteData)
        buildStructureRow(level, rowNumbers, member, subRouteData, subRows) +: subRows
    }
  }

  private def buildStructureRow(
    level: Long,
    rowNumbers: Seq[Int],
    member: RouteMemberInfo,
    subRouteData: SubRouteData,
    subRows: Seq[RouteStructureRow]
  ): RouteStructureRow = {
    val distance = subRouteData.distance
    val subRowsDistance = subRows.flatMap(_.relation.map(_.totalDistance)).sum
    val totalDistance = distance + subRowsDistance
    val rowNumber = rowNumbers.mkString(".")
    RouteStructureRow(
      rowNumber = rowNumber,
      level = level,
      id = member.id,
      memberType = member.memberType,
      role = member.role,
      link = None,
      distance = distance,
      name = member.name,
      poi = member.poi,
      way = None,
      relation = Some(
        RouteStructureRelation(
          physical = false,
          name = subRouteData.name,
          subRelationIndex = None,
          survey = None,
          symbol = None,
          segments = subRouteData.segments,
          totalDistance = totalDistance,
          gaps = None,
          happy = false,
        )
      )
    )
  }

  private def buildSubRelationRows(level: Long, rowNumbers: Seq[Int], member: RouteMemberInfo, processedRelationIds: Seq[Long], subRouteData: SubRouteData): Seq[RouteStructureRow] = {
    val subRelationMembers = subRouteData.members.filter(_.memberType == MemberType.Relation)
    subRelationMembers.zipWithIndex.flatMap { case (subRelationMember, index) =>
      buildRelationRows(level + 1, rowNumbers :+ (index + 1), subRelationMember, processedRelationIds :+ member.id)
    }
  }
}
