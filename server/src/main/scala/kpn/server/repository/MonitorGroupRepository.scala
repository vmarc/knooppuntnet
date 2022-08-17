package kpn.server.repository

import kpn.api.base.MongoId
import kpn.api.common.monitor.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute

trait MonitorGroupRepository {

  def groupByName(groupName: String): Option[MonitorGroup]

  def groupById(groupId: MongoId): Option[MonitorGroup]

  def groups(): Seq[MonitorGroup]

  def saveGroup(routeGroup: MonitorGroup): Unit

  def deleteGroup(name: String): Unit

  def groupRoutes(groupName: String): Seq[MonitorRoute]

}
