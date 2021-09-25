package kpn.server.repository

import kpn.api.common.monitor.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute

trait MonitorGroupRepository {

  def group(groupName: String): Option[MonitorGroup]

  def groups(): Seq[MonitorGroup]

  def saveGroup(routeGroup: MonitorGroup): Unit

  def deleteGroup(name: String): Unit

  def groupRoutes(groupName: String): Seq[MonitorRoute]

}
