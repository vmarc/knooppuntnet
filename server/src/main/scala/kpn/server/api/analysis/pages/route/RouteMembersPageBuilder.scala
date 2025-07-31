package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteMembersPage
import kpn.api.common.route.StructureRow
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteMembersPageBuilder(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository
) {
  def build(language: Language, routeId: Long): Option[RouteMembersPage] = {
    routeRepository.findRouteById(routeId).map { routeDoc =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val networkReferences = routeRepository.networkReferences(routeId)

      val routeInfo = RouteInfo(
        routeDoc._id,
        routeDoc.summary.name,
        routeDoc.summary.routeTypes,
        memberCount = routeDoc.structureRows.size,
        pathCount = routeDoc.paths.size,
        segmentCount = routeDoc.segments.size,
        changeCount = changeCount,
        bounds = routeDoc.bounds,
      )

      val structureRows = routeDoc.structureRows.map { row =>
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

      RouteMembersPage(
        routeInfo,
        structureRows,
      )
    }
  }
}
