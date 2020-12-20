package kpn.server.repository

import kpn.api.common.monitor.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute

trait MonitorAdminGroupRepository {

  def groups(): Seq[MonitorGroup]

  def group(groupName: String): Option[MonitorGroup]

  def saveGroup(routeGroup: MonitorGroup): Unit

  def deleteGroup(id: String): Unit

  def groupRoutes(groupName: String): Seq[MonitorRoute]

}
