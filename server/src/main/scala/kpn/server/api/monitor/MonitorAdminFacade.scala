package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorAdminGroupPage
import kpn.api.common.monitor.MonitorGroup
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.custom.ApiResponse
import kpn.server.api.monitor.domain.MonitorRoute

trait MonitorAdminFacade {

  def groups(user: Option[String]): ApiResponse[MonitorGroupsPage]

  def group(user: Option[String], groupName: String): ApiResponse[MonitorAdminGroupPage]

  def addGroup(user: Option[String], group: MonitorGroup): Unit

  def updateGroup(user: Option[String], group: MonitorGroup): Unit

  def deleteGroup(user: Option[String], groupName: String): Unit

  def addRoute(user: Option[String], groupName: String, route: MonitorRoute): Unit

  def updateRoute(user: Option[String], groupName: String, route: MonitorRoute): Unit

  def deleteRoute(user: Option[String], groupName: String, routeName: String): Unit

}
