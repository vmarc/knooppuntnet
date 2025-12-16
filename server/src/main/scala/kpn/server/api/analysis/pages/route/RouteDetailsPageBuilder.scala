package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.location.LocationCandidateInfo
import kpn.api.common.route.RouteDetails
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteInfo
import kpn.database.actions.routes.RouteDetailsData
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
      buildPage(language, routeId)
    }
  }

  private def buildPage(language: Language, routeId: Long): Option[RouteDetailsPage] = {
    routeRepository.routeDetails(routeId).map { routeDetailsData =>
      val details = buildDetails(language, routeDetailsData)
      val routeInfo = buildRouteInfo(routeId, details)
      RouteDetailsPage(
        routeInfo,
        details,
      )
    }
  }

  private def buildDetails(language: Language, routeDetailsData: RouteDetailsData) = {
    val locationCandidateInfos = buildLocationCandidateInfos(language, routeDetailsData)
    RouteDetails.from(routeDetailsData, locationCandidateInfos)
  }

  private def buildLocationCandidateInfos(language: Language, routeDetailsData: RouteDetailsData) = {
    routeDetailsData.locationAnalysis.candidates.map { candidate =>
      val locationNames = candidate.location.names
      val locationInfos = locationService.toInfos(language, locationNames, locationNames)
      LocationCandidateInfo(locationInfos, candidate.percentage)
    }
  }

  private def buildRouteInfo(routeId: Long, details: RouteDetails) = {
    val changeCount = changeSetRepository.routeChangesCount(routeId)
    RouteInfo(
      routeId,
      details.summary.name,
      details.summary.routeTypes,
      bounds = details.bounds,
      memberCount = details.memberCount,
      pathCount = details.pathCount,
      segmentCount = details.segmentCount,
      changeCount = changeCount,
    )
  }
}
