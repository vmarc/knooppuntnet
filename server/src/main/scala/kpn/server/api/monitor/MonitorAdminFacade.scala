package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorAdminGroupPage
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.RouteGroupsPage
import kpn.api.custom.ApiResponse

trait MonitorAdminFacade {

  def groups(user: Option[String]): ApiResponse[RouteGroupsPage]

  def group(user: Option[String], groupName: String): ApiResponse[MonitorAdminGroupPage]

  def addGroup(user: Option[String], group: MonitorGroup): Unit

  def updateGroup(user: Option[String], group: MonitorGroup): Unit

  def deleteGroup(user: Option[String], groupName: String): Unit

}
