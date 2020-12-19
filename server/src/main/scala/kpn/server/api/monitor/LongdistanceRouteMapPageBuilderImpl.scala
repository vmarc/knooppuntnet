package kpn.server.api.monitor

import kpn.api.common.monitor.LongdistanceRouteMapPage
import kpn.server.repository.LongdistanceRouteRepository
import org.springframework.stereotype.Component

@Component
class LongdistanceRouteMapPageBuilderImpl(
  longdistanceRouteRepository: LongdistanceRouteRepository
) extends LongdistanceRouteMapPageBuilder {

  override def build(routeId: Long): Option[LongdistanceRouteMapPage] = {
    longdistanceRouteRepository.routeWithId(routeId).map { route =>
      LongdistanceRouteMapPage(
        route.id,
        route.ref,
        route.name,
        route.nameNl,
        route.nameEn,
        route.nameDe,
        route.nameFr,
        route.bounds,
        route.gpxFilename,
        route.osmSegments,
        route.gpxGeometry,
        route.okGeometry,
        route.nokSegments
      )
    }
  }

}
