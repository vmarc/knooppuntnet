package kpn.server.repository

import kpn.api.common.monitor.MonitorRouteGroup

trait MonitorRouteGroupRepository {

  def all(): Seq[MonitorRouteGroup]

}
