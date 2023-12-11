package kpn.server.monitor.repository

import kpn.api.base.ObjectId
import kpn.server.monitor.domain.MonitorGroup
import kpn.server.monitor.domain.MonitorRoute

trait MonitorGroupRepository {

  def groupByName(groupName: String): Option[MonitorGroup]

  def groupById(groupId: ObjectId): Option[MonitorGroup]

  def groups(): Seq[MonitorGroup]

  def saveGroup(routeGroup: MonitorGroup): Unit

  def deleteGroup(groupId: ObjectId): Unit

  def groupRoutes(groupId: ObjectId): Seq[MonitorRoute]

  def groupRouteIds(groupId: ObjectId): Seq[ObjectId]

}
