package kpn.server.api.analysis.pages.route

import kpn.api.common.route.RouteMapPage
import kpn.server.repository.ChangeSetRepository
import kpn.server.repository.RouteRepository
import org.springframework.stereotype.Component

@Component
class RouteMapPageBuilderImpl(
  routeRepository: RouteRepository,
  changeSetRepository: ChangeSetRepository
) extends RouteMapPageBuilder {

  override def build(routeId: Long): Option[RouteMapPage] = {
    if (routeId == 1) {
      Some(RouteMapPageExample.page)
    }
    else {
      doBuild(routeId)
    }
  }

  private def doBuild(routeId: Long): Option[RouteMapPage] = {
    routeRepository.mapInfo(routeId).map { routeMapInfo =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      RouteMapPage(routeMapInfo, changeCount)
    }
  }
}
