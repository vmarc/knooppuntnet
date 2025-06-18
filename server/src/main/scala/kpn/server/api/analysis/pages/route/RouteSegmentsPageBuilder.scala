package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteSegmentsPage
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteSegmentsPageBuilder(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository,
) {
  def build(language: Language, routeId: Long): Option[RouteSegmentsPage] = {
    if (routeId == 1) {
      Some(RouteSegmentsPageExample.page)
    }
    else {
      doBuildSegmentsPage(language, routeId)
    }
  }

  private def doBuildSegmentsPage(language: Language, routeId: Long): Option[RouteSegmentsPage] = {
    routeRepository.routeSegments(routeId).map { routeSegmentData =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val segmentCount = routeSegmentData.segments.length
      val routeInfo = RouteInfo(
        routeId,
        routeName = routeSegmentData.name,
        routeTypes = routeSegmentData.routeTypes,
        changeCount = changeCount,
        segmentCount = routeSegmentData.segments.length,
      )
      RouteSegmentsPage(
        routeInfo,
        routeSegmentData.segments,
      )
    }
  }
}
