package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteDetailsPageBuilderImpl(
  monitorRouteRepository: MonitorRouteRepository
) extends MonitorRouteDetailsPageBuilder {

  override def build(routeId: Long): Option[MonitorRouteDetailsPage] = {
    monitorRouteRepository.routeWithId(routeId).map { route =>
      MonitorRouteDetailsPage(
        route.id,
        route.ref,
        route.name,
        route.nameNl,
        route.nameEn,
        route.nameDe,
        route.nameFr,
        route.description,
        route.operator,
        route.website,
        route.wayCount,
        route.osmDistance,
        route.gpxDistance,
        route.gpxFilename,
        route.gpxFilename.isDefined && route.osmSegments.size == 1 && route.nokSegments.isEmpty,
        route.osmSegments.size,
        route.nokSegments.size
      )
    }
  }
}
