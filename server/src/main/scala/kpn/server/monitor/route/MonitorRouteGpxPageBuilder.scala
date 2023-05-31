package kpn.server.monitor.route

import kpn.api.common.monitor.MonitorRouteGpxPage
import kpn.core.common.Time
import kpn.server.monitor.repository.MonitorGroupRepository
import kpn.server.monitor.repository.MonitorRouteRepository
import kpn.server.monitor.MonitorUtil
import org.springframework.stereotype.Component

@Component
class MonitorRouteGpxPageBuilder(
  monitorGroupRepository: MonitorGroupRepository,
  monitorRouteRepository: MonitorRouteRepository
) {

  def build(groupName: String, routeName: String, subRelationId: Long): Option[MonitorRouteGpxPage] = {
    monitorGroupRepository.groupByName(groupName).flatMap { group =>
      monitorRouteRepository.routeByName(group._id, routeName).flatMap { route =>
        MonitorUtil.subRelation(route, subRelationId).map { monitorRouteRelation =>
          val referenceTimestamp = monitorRouteRelation.referenceTimestamp match {
            case None => Time.now
            case Some(timestamp) => timestamp
          }
          MonitorRouteGpxPage(
            group.name,
            route.name,
            subRelationId,
            monitorRouteRelation.name,
            referenceTimestamp,
            monitorRouteRelation.referenceFilename,
            monitorRouteRelation.referenceDistance
          )
        }
      }
    }
  }
}
