package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteMembersPage
import kpn.api.common.route.StructureRow
import kpn.core.doc.RouteDoc
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("web"))
class RouteMembersPageBuilder(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository
) {

  def build(language: Language, routeId: Long): Option[RouteMembersPage] = {
    routeRepository.findRouteById(routeId).map { routeDoc =>
      val routeInfo = buildRouteInfo(routeDoc)
      val structureRows = buildStructureRows(routeDoc)
      RouteMembersPage(
        routeInfo,
        structureRows,
      )
    }
  }

  private def buildRouteInfo(routeDoc: RouteDoc) = {
    val changeCount = changeSetRepository.routeChangesCount(routeDoc._id)
    RouteInfo(
      routeDoc._id,
      routeDoc.base.name,
      routeDoc.base.routeTypes,
      memberCount = routeDoc.structureRows.size,
      pathCount = routeDoc.paths.size,
      segmentCount = routeDoc.segments.size,
      changeCount = changeCount,
      bounds = routeDoc.bounds,
    )
  }

  private def buildStructureRows(routeDoc: RouteDoc): Seq[StructureRow] = {
    routeDoc.structureRows.map { row =>
      StructureRow(
        rowNumber = row.rowNumber,
        level = 0,
        id = row.id,
        memberType = row.memberType,
        role = row.role,
        link = row.link,
        distance = row.distance,
        name = row.name,
        poi = row.poi,
        way = row.way,
        relation = row.relation,
        segmentIds = row.segmentIds,
        pathIds = row.pathIds,
        physical = false,
        relationId = 0,
        subRelationIndex = None,
        survey = None,
        symbol = None,
        referenceType = None,
        referenceTimestamp = None,
        referenceFilename = None,
        referenceDistance = 0,
        deviationDistance = None,
        deviationCount = None,
        osmSegmentCount = Some(row.segmentIds.length),
        osmDistance = row.distance,
        osmDistanceSubRelations = 0,
        gaps = None,
        showMap = false,
        happy = false
      )
    }
  }
}
