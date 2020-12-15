package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.server.repository.ChangeSetInfoRepository
import kpn.server.repository.MonitorRouteRepository
import org.springframework.stereotype.Component

@Component
class MonitorRouteChangePageBuilderImpl(
  monitorRouteRepository: MonitorRouteRepository,
  changeSetInfoRepository: ChangeSetInfoRepository
) extends MonitorRouteChangePageBuilder {

  override def build(routeId: Long, changeId: Long): Option[MonitorRouteChangePage] = {

    val comment = changeSetInfoRepository.get(changeId).flatMap(_.tags("comment"))

    monitorRouteRepository.change(routeId, changeId).flatMap { change =>
      val changeWithComment = change.copy(comment = comment)
      monitorRouteRepository.routeWithId(routeId).map { route =>
        MonitorRouteChangePage(
          route.id,
          route.ref,
          route.name,
          changeWithComment
        )
      }
    }
  }

}
