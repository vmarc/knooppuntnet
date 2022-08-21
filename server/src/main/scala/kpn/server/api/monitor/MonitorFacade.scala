package kpn.server.api.monitor

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorGroupProperties
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorRouteAdd
import kpn.api.common.monitor.MonitorRouteAddPage
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.custom.ApiResponse
import kpn.server.api.monitor.domain.MonitorRoute

import scala.xml.Elem

trait MonitorFacade {

  def changes(user: Option[String], parameters: MonitorChangesParameters): ApiResponse[MonitorChangesPage]

  def groups(user: Option[String]): ApiResponse[MonitorGroupsPage]

  def groupNames(user: Option[String]): ApiResponse[Seq[String]]

  def group(user: Option[String], groupName: String): ApiResponse[MonitorGroupPage]

  def addGroup(user: Option[String], properties: MonitorGroupProperties): Unit

  def updateGroup(user: Option[String], id: ObjectId, properties: MonitorGroupProperties): Unit

  def deleteGroup(user: Option[String], groupId: ObjectId): Unit

  def groupChanges(user: Option[String], groupName: String, parameters: MonitorChangesParameters): ApiResponse[MonitorGroupChangesPage]

  def route(user: Option[String], groupName: String, routeName: String): ApiResponse[MonitorRouteDetailsPage]

  def routeMap(user: Option[String], groupName: String, routeName: String): ApiResponse[MonitorRouteMapPage]

  def routeChanges(user: Option[String], monitorRouteId: String, parameters: MonitorChangesParameters): ApiResponse[MonitorRouteChangesPage]

  def routeChange(user: Option[String], routeId: Long, changeSetId: Long, replicationId: Long): ApiResponse[MonitorRouteChangePage]

  def routeInfo(user: Option[String], routeId: Long): ApiResponse[MonitorRouteInfoPage]

  def groupRouteAdd(user: Option[String], groupName: String): ApiResponse[MonitorRouteAddPage]

  def addRoute(user: Option[String], groupName: String, route: MonitorRouteAdd): Unit

  def updateRoute(user: Option[String], route: MonitorRoute): Unit

  def deleteRoute(user: Option[String], groupName: String, routeName: String): Unit

  def processNewReference(user: Option[String], groupName: String, routeName: String, filename: String, xml: Elem): Unit

  def routeNames(user: Option[String], groupId: ObjectId): ApiResponse[Seq[String]]

}
