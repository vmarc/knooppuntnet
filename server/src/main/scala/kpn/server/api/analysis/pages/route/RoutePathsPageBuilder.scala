package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RoutePathsPage
import kpn.core.util.Util
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RoutePathsPageBuilder(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository
) {
  def build(language: Language, routeId: Long): Option[RoutePathsPage] = {
    routeRepository.findRouteById(routeId).map { routeDoc =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val segmentCount = routeDoc.segments.size

      val routeBounds = Util.mergeBounds(routeDoc.segments.map(_.bounds))

      val routeInfo = RouteInfo(
        routeDoc._id,
        routeDoc.summary.name,
        routeDoc.summary.routeTypes,
        changeCount,
        segmentCount,
        routeDoc.bounds
      )

      RoutePathsPage(
        routeInfo,
        routeDoc.paths,
      )
    }
  }
}
