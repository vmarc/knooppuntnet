package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteDetail
import kpn.api.common.monitor.MonitorRoutesPage
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRoutesPageBuilderImpl(
  monitorRouteRepository: MonitorRouteRepository
) extends MonitorRoutesPageBuilder {

  override def build(): Option[MonitorRoutesPage] = {

    val routes = monitorRouteRepository.all()
    val sortedRoutes = routes.sortWith { (a, b) =>
      val ref1 = a.ref.getOrElse("zz")
      val ref2 = b.ref.getOrElse("zz")
      if (ref1 == ref2) {
        a.name < b.name
      }
      else {
        ref1 < ref2
      }
    }

    val details = sortedRoutes.map { route =>
      MonitorRouteDetail(
        route.id,
        route.ref,
        route.name,
        route.description,
        route.operator,
        route.website,
        route.wayCount,
        route.osmDistance,
        route.gpxDistance,
        route.gpxFilename,
        route.osmSegments.size == 1,
        route.gpxFilename.isDefined && route.nokSegments.isEmpty
      )
    }
    Some(
      MonitorRoutesPage(
        details
      )
    )
  }

}
