package kpn.server.api.analysis.pages.route

import kpn.api.common.Language
import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RoutePathsPage
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

      val routeInfo = RouteInfo(
        routeDoc._id,
        routeDoc.base.name,
        routeDoc.base.routeTypes,
        memberCount = routeDoc.structureRows.size,
        pathCount = routeDoc.paths.size,
        segmentCount = routeDoc.segments.size,
        changeCount = changeCount,
        bounds = routeDoc.bounds,
      )

      RoutePathsPage(
        routeInfo,
        routeDoc.paths,
      )
    }
  }
}
