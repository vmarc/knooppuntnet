package kpn.server.repository

import kpn.api.common.monitor.MonitorRouteGroup

trait MonitorAdminRouteGroupRepository {

  def all(stale: Boolean): Seq[MonitorRouteGroup]

  def group(groupName: String): Option[MonitorRouteGroup]

  def saveGroup(routeGroup: MonitorRouteGroup): Unit

  def deleteGroup(id: String): Unit

}
