package kpn.server.repository

import kpn.api.base.ObjectId
import kpn.server.api.monitor.domain.MonitorGroup
import kpn.server.api.monitor.domain.MonitorRoute
import kpn.server.api.monitor.domain.OldMonitorRoute

trait MonitorGroupRepository {

  def groupByName(groupName: String): Option[MonitorGroup]

  def groupById(groupId: ObjectId): Option[MonitorGroup]

  def groups(): Seq[MonitorGroup]

  def saveGroup(routeGroup: MonitorGroup): Unit

  def deleteGroup(groupId: ObjectId): Unit

  def groupRoutes(groupId: ObjectId): Seq[MonitorRoute]

  def groupRouteIds(groupId: ObjectId): Seq[ObjectId]

  def oldGroupRoutes(groupId: ObjectId): Seq[OldMonitorRoute]
}
