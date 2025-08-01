package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.location.LocationCandidateInfo
import kpn.api.common.route.RouteDetails
import kpn.api.common.route.RouteDetailsPage
import kpn.api.common.route.RouteInfo
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
    routeRepository.routeDetails(routeId).map { routeDetailsData =>

      val changeCount = changeSetRepository.routeChangesCount(routeId)

      val locationCandidateInfos = {
        routeDetailsData.locationAnalysis.candidates.map { candidate =>
          val locationNames = candidate.location.names
          val locationInfos = locationService.toInfos(language, locationNames, locationNames)
          LocationCandidateInfo(locationInfos, candidate.percentage)
        }
      }

      val details = RouteDetails.from(routeDetailsData, locationCandidateInfos)

      val routeInfo = RouteInfo(
        details.summary.id,
        details.summary.name,
        details.summary.routeTypes,
        bounds = details.bounds,
        memberCount = details.memberCount,
        pathCount = details.pathCount,
        segmentCount = details.segmentCount,
        changeCount = changeCount,
      )

      RouteDetailsPage(
        routeInfo,
        details,
      )
    }
  }
}
