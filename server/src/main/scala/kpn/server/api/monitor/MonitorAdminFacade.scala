package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorAdminGroupPage
import kpn.api.common.monitor.MonitorAdminGroupsPage
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.custom.ApiResponse

trait MonitorAdminFacade {

  def groups(user: Option[String]): ApiResponse[MonitorAdminGroupsPage]

  def group(user: Option[String], groupName: String): ApiResponse[MonitorAdminGroupPage]

  def addGroup(user: Option[String], group: MonitorGroup): Unit

  def updateGroup(user: Option[String], group: MonitorGroup): Unit

  def deleteGroup(user: Option[String], groupName: String): Unit

}
