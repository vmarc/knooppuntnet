package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteDetailsPageBuilderImpl(
  monitorRouteRepository: MonitorRouteRepository
) extends MonitorRouteDetailsPageBuilder {

  override def build(routeId: Long): Option[MonitorRouteDetailsPage] = {
    monitorRouteRepository.route(routeId).flatMap { route =>
      monitorRouteRepository.routeState(routeId).map { routeState =>
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
          routeState.wayCount,
          routeState.osmDistance,
          routeState.gpxDistance,
          None, // TODO routeState.gpxFilename,
          happy = false, // TODO routeState.gpxFilename.isDefined && route.osmSegments.size == 1 && route.nokSegments.isEmpty,
          routeState.osmSegments.size,
          routeState.nokSegments.size
        )
      }
    }
  }
}
