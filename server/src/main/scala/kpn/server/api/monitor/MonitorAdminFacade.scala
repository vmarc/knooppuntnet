package kpn.server.api.monitor

import kpn.api.common.monitor.MonitorAdminRouteGroupPage
import kpn.api.common.monitor.MonitorRouteGroup
import kpn.api.custom.ApiResponse

trait MonitorAdminFacade {

  def group(user: Option[String], groupName: String): ApiResponse[MonitorAdminRouteGroupPage]

  def addGroup(user: Option[String], group: MonitorRouteGroup): Unit

  def updateGroup(user: Option[String], group: MonitorRouteGroup): Unit

  def deleteGroup(user: Option[String], groupName: String): Unit

}
