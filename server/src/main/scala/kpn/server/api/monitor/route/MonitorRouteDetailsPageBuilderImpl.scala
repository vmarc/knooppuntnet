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

  override def build(monitorRouteId: String): Option[MonitorRouteDetailsPage] = {
    monitorRouteRepository.route(monitorRouteId).flatMap { route =>
      monitorGroupRepository.group(route.groupName).map { group =>
        val routeStateOption = monitorRouteRepository.routeState(monitorRouteId)
        MonitorRouteDetailsPage(
          route._id,
          route.routeId,
          route.routeName,
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
          routeStateOption.map(_.wayCount).getOrElse(0L),
          routeStateOption.map(_.osmDistance).getOrElse(0L),
          routeStateOption.map(_.gpxDistance).getOrElse(0L),
          None, // TODO routeState.gpxFilename,
          happy = false, // TODO routeState.gpxFilename.isDefined && route.osmSegments.size == 1 && route.nokSegments.isEmpty,
          routeStateOption.map(_.osmSegments.size.toLong).getOrElse(0L),
          routeStateOption.map(_.nokSegments.size.toLong).getOrElse(0L)
        )
      }
    }
  }
}
