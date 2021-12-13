package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.server.repository.MonitorGroupRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteDetailsPageBuilderImpl(
  monitorRouteRepository: MonitorRouteRepository,
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorRouteDetailsPageBuilder {

  override def build(routeId: Long): Option[MonitorRouteDetailsPage] = {
    monitorRouteRepository.route(routeId).flatMap { route =>
      monitorRouteRepository.routeState(routeId).flatMap { routeState =>
        monitorGroupRepository.group(route.groupName).map { group =>
          MonitorRouteDetailsPage(
            route.routeId,
            route.name,
            group.name,
            group.description,
            route.nameNl,
            route.nameEn,
            route.nameDe,
            route.nameFr,
            route.ref,
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
}
