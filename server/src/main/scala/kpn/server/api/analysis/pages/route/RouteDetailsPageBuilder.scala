package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.location.LocationCandidateInfo
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteDetailsPageData
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

      // TODO add routeIds, parent routes (reverse subRelationTree), add children
      val data = RouteDetailsPageData(
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
        routeDoc.structureRows,
        routeDoc.nameDerivedFromNodes,
        routeDoc.nodes,
        routeDoc.bounds,
        routeDoc.routeIds,
        routeDoc.parentRoutes
      )
      RouteDetailsPage(
        data,
        networkReferences,
        changeCount,
        segmentCount
      )
    }
  }
}
