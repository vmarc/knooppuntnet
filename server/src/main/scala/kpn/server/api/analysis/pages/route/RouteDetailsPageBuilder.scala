package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.location.LocationCandidateInfo
import kpn.api.common.route.RouteDetails
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.StructureRow
import kpn.core.util.Util
import kpn.server.analyzer.engine.analysis.location.LocationService
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteDetailsPageBuilder(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository,
  locationService: LocationService
) {
  def build(language: Language, routeId: Long): Option[RouteDetailsPage] = {
    if (routeId == 1) {
      Some(RouteDetailsPageExample.page)
    }
    else {
      doBuildDetailsPage(language, routeId)
    }
  }

  private def doBuildDetailsPage(language: Language, routeId: Long): Option[RouteDetailsPage] = {
    routeRepository.findRouteById(routeId).map { routeDoc =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val segmentCount = routeDoc.segments.size
      val networkReferences = routeRepository.networkReferences(routeId)
      val locationCandidateInfos = {
        routeDoc.locationAnalysis.candidates.map { candidate =>
          val locationNames = candidate.location.names
          val locationInfos = locationService.toInfos(language, locationNames, locationNames)
          LocationCandidateInfo(locationInfos, candidate.percentage)
        }
      }

      val routeBounds = Util.mergeBounds(routeDoc.segments.map(_.bounds))

      val routeInfo = RouteInfo(
        routeDoc._id,
        routeDoc.summary.name,
        routeDoc.summary.routeTypes,
        changeCount,
        segmentCount,
        routeDoc.bounds
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

      // TODO add routeIds, parent routes (reverse subRelationTree), add children
      val data = RouteDetails(
        routeDoc._id,
        routeDoc.active,
        routeDoc.summary,
        routeDoc.proposed,
        routeDoc.version,
        routeDoc.changeSetId,
        routeDoc.lastUpdated,
        routeDoc.lastSurvey,
        routeDoc.facts,
        locationCandidateInfos,
        routeDoc.unexpectedNodeIds,
        routeDoc.unexpectedRelationIds,
        routeDoc.segments,
        routeDoc.paths,
        routeDoc.nameDerivedFromNodes,
        routeDoc.nodes,
        routeDoc.bounds,
        routeDoc.routeIds,
        routeDoc.parentRoutes,
        networkReferences,
      )
      RouteDetailsPage(
        routeInfo,
        data,
      )
    }
  }
}
