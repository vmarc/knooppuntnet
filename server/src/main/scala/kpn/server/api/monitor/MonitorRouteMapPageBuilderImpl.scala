package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteMapPageBuilderImpl(
  monitorRouteRepository: MonitorRouteRepository
) extends MonitorRouteMapPageBuilder {

  override def build(routeId: Long): Option[MonitorRouteMapPage] = {
    monitorRouteRepository.routeWithId(routeId).map { route =>
      MonitorRouteMapPage(
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
