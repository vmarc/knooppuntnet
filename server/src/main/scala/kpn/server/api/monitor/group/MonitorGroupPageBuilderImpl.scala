package kpn.server.api.monitor.group

import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorRouteDetail
import kpn.server.repository.MonitorGroupRepository
import org.springframework.stereotype.Component

@Component
class MonitorGroupPageBuilderImpl(
  monitorGroupRepository: MonitorGroupRepository
) extends MonitorGroupPageBuilder {

  override def build(groupName: String): Option[MonitorGroupPage] = {
    val routes = monitorGroupRepository.groupRoutes(groupName)
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

    Some(
      MonitorGroupPage(
        sortedRoutes.map { route =>
          MonitorRouteDetail(
            route.id,
            route.ref,
            route.name,
            route.description,
            route.operator,
            route.website,
            0,
            0,
            0,
            None,
            osmHappy = false,
            gpxHappy = false
          )
        }
      )
    )
  }
}
