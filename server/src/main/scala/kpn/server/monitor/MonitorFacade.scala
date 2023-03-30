package kpn.server.monitor

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorChangesPage
import kpn.api.common.monitor.MonitorChangesParameters
import kpn.api.common.monitor.MonitorGroupChangesPage
import kpn.api.common.monitor.MonitorGroupPage
import kpn.api.common.monitor.MonitorGroupProperties
import kpn.api.common.monitor.MonitorGroupsPage
import kpn.api.common.monitor.MonitorRouteAddPage
import kpn.api.common.monitor.MonitorRouteChangePage
import kpn.api.common.monitor.MonitorRouteChangesPage
import kpn.api.common.monitor.MonitorRouteDetailsPage
import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.common.monitor.MonitorRouteUpdatePage
import kpn.api.custom.ApiResponse
import kpn.api.custom.Day

import javax.servlet.http.HttpServletRequest
import scala.xml.Elem

trait MonitorFacade {

  def changes(
    request: HttpServletRequest,
    user: Option[String],
    parameters: MonitorChangesParameters
  ): ApiResponse[MonitorChangesPage]

  def groups(
    request: HttpServletRequest,
    user: Option[String]
  ): ApiResponse[MonitorGroupsPage]

  def groupNames(
    request: HttpServletRequest,
    user: Option[String]
  ): ApiResponse[Seq[String]]

  def group(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String
  ): ApiResponse[MonitorGroupPage]

  def groupAdd(
    request: HttpServletRequest,
    user: Option[String],
    properties: MonitorGroupProperties
  ): Unit

  def groupUpdate(
    request: HttpServletRequest,
    user: Option[String],
    id: ObjectId,
    properties: MonitorGroupProperties
  ): Unit

  def groupDelete(
    request: HttpServletRequest,
    user: Option[String],
    groupId: ObjectId
  ): Unit

  def groupChanges(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    parameters: MonitorChangesParameters
  ): ApiResponse[MonitorGroupChangesPage]

  def route(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String
  ): ApiResponse[MonitorRouteDetailsPage]

  def routeMap(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String,
    relationId: Option[Long]
  ): ApiResponse[MonitorRouteMapPage]

  def routeChanges(
    request: HttpServletRequest,
    user: Option[String],
    monitorRouteId: String,
    parameters: MonitorChangesParameters
  ): ApiResponse[MonitorRouteChangesPage]

  def routeChange(
    request: HttpServletRequest,
    user: Option[String],
    routeId: Long,
    changeSetId: Long,
    replicationId: Long
  ): ApiResponse[MonitorRouteChangePage]

  def routeInfo(
    request: HttpServletRequest,
    user: Option[String],
    routeId: Long
  ): ApiResponse[MonitorRouteInfoPage]

  def groupRouteAdd(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String
  ): ApiResponse[MonitorRouteAddPage]

  def routeUpdatePage(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String
  ): ApiResponse[MonitorRouteUpdatePage]

  def routeAdd(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    properties: MonitorRouteProperties
  ): ApiResponse[MonitorRouteSaveResult]

  def routeUpdate(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String,
    properties: MonitorRouteProperties
  ): ApiResponse[MonitorRouteSaveResult]

  def routeDelete(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String
  ): Unit

  def upload(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String,
    routeName: String,
    relationId: Long,
    referenceDay: Day,
    filename: String,
    xml: Elem
  ): ApiResponse[MonitorRouteSaveResult]

  def routeNames(
    request: HttpServletRequest,
    user: Option[String],
    groupName: String
  ): ApiResponse[Seq[String]]

}
