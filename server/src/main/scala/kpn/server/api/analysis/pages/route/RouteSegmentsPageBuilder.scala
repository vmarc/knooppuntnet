package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
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
    routeRepository.routeSegments(routeId).map { routeSegments =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val segmentCount = routeSegments.segments.length
      RouteSegmentsPage(
        routeSegments,
        changeCount,
        segmentCount
      )
    }
  }
}
