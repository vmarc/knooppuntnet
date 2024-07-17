package kpn.server.api.analysis.pages.route

import kpn.api.common.route.RouteMapPage
import kpn.core.util.Util
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteMapPageBuilder(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository
) {

  def build(routeId: Long): Option[RouteMapPage] = {
    routeRepository.mapInfo(routeId).map { routeMapInfo =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val bounds = Util.mergeBounds(routeMapInfo.segments.map(_.bounds))
      RouteMapPage(routeMapInfo, bounds, changeCount)
    }
  }
}
