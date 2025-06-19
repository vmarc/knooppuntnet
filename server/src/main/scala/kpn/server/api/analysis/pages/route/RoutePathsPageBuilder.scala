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
  changeSetRepository: ChangeSetRepository,
) {
  def build(language: Language, routeId: Long): Option[RoutePathsPage] = {
    routeRepository.routePaths(routeId).map { routePathData =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val pathCount = routePathData.paths.length
      val routeInfo = RouteInfo(
        routeId,
        routeName = routePathData.name,
        routeTypes = routePathData.routeTypes,
        changeCount = changeCount,
        segmentCount = routePathData.paths.length,
        bounds = routePathData.bounds,
      )
      RoutePathsPage(
        routeInfo,
        routePathData.paths,
      )
    }
  }
}
