package kpn.server.repository

import kpn.api.common.monitor.MonitorRouteGroup

trait MonitorRouteGroupRepository {

  def all(stale: Boolean = true): Seq[MonitorRouteGroup]

}
