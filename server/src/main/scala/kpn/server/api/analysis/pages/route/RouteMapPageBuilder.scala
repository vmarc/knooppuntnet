package kpn.server.api.analysis.pages.route

import kpn.api.common.route.RouteInfo
import kpn.api.common.route.RouteMapInfo
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
    routeRepository.mapData(routeId).map { routeMapData =>
      val changeCount = changeSetRepository.routeChangesCount(routeId)
      val bounds = Util.mergeBounds(routeMapData.segments.map(_.bounds))
      val routeInfo = RouteInfo(
        routeId = routeMapData.routeId,
        routeName = routeMapData.routeName,
        routeTypes = routeMapData.routeTypes,
        changeCount = changeCount,
        segmentCount = routeMapData.segments.size,
      )
      val routeMapInfo = RouteMapInfo(
        segments = routeMapData.segments,
        paths = routeMapData.paths,
      )
      RouteMapPage(
        routeInfo,
        routeMapInfo,
        bounds
      )
    }
  }
}
