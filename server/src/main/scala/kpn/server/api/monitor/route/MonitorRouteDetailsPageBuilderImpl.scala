package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
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
    monitorRouteRepository.routeById(ObjectId("TODO") /*monitorRouteId*/).flatMap { route =>
      monitorGroupRepository.groupById(route.groupId).map { group =>
        val routeStateOption = monitorRouteRepository.routeState(monitorRouteId)
        val happy = routeStateOption match {
          case Some(routeState) =>
            routeState.gpxDistance > 0 &&
              routeState.nokSegments.isEmpty &&
              routeState.osmSegments.size == 1
          case None => false
        }
        MonitorRouteDetailsPage(
          route._id.oid,
          group.name,
          group.description,
          route.name,
          route.description,
          route.relationId,
          routeStateOption.map(_.wayCount).getOrElse(0L),
          routeStateOption.map(_.osmDistance).getOrElse(0L),
          routeStateOption.map(_.gpxDistance).getOrElse(0L),
          None, // TODO routeState.gpxFilename,
          happy = happy,
          routeStateOption.map(_.osmSegments.size.toLong).getOrElse(0L),
          routeStateOption.map(_.nokSegments.size.toLong).getOrElse(0L)
        )
      }
    }
  }
}
