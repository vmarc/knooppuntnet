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
import kpn.api.common.monitor.MonitorRouteGpxPage
import kpn.api.common.monitor.MonitorRouteInfoPage
import kpn.api.common.monitor.MonitorRouteMapPage
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.common.monitor.MonitorRouteUpdatePage
import kpn.api.custom.ApiResponse
import kpn.api.custom.Timestamp

import scala.xml.Elem

trait MonitorFacade {

  def changes(parameters: MonitorChangesParameters): ApiResponse[MonitorChangesPage]

  def groups(): ApiResponse[MonitorGroupsPage]

  def groupNames(): ApiResponse[Seq[String]]

  def group(groupName: String): ApiResponse[MonitorGroupPage]

  def groupAdd(properties: MonitorGroupProperties): Unit

  def groupUpdate(id: ObjectId, properties: MonitorGroupProperties): Unit

  def groupDelete(groupId: ObjectId): Unit

  def groupChanges(groupName: String, parameters: MonitorChangesParameters): ApiResponse[MonitorGroupChangesPage]

  def route(groupName: String, routeName: String): ApiResponse[MonitorRouteDetailsPage]

  def routeMap(groupName: String, routeName: String, relationId: Option[Long]): ApiResponse[MonitorRouteMapPage]

  def routeGpx(groupName: String, routeName: String, subRelationId: Long): ApiResponse[MonitorRouteGpxPage]

  def routeResetSubRelationGpxReference(groupName: String, routeName: String, subRelationId: Long): Unit

  def routeChanges(monitorRouteId: String, parameters: MonitorChangesParameters): ApiResponse[MonitorRouteChangesPage]

  def routeChange(routeId: Long, changeSetId: Long, replicationId: Long): ApiResponse[MonitorRouteChangePage]

  def routeInfo(routeId: Long): ApiResponse[MonitorRouteInfoPage]

  def groupRouteAdd(groupName: String): ApiResponse[MonitorRouteAddPage]

  def routeUpdatePage(groupName: String, routeName: String): ApiResponse[MonitorRouteUpdatePage]

  def routeAdd(groupName: String, properties: MonitorRouteProperties): ApiResponse[MonitorRouteSaveResult]

  def routeUpdate(groupName: String, routeName: String, properties: MonitorRouteProperties): ApiResponse[MonitorRouteSaveResult]

  def routeDelete(groupName: String, routeName: String): Unit

  def upload(
    groupName: String,
    routeName: String,
    relationId: Option[Long],
    referenceTimestamp: Timestamp,
    filename: String,
    xml: Elem
  ): ApiResponse[MonitorRouteSaveResult]

  def routeAnalyze(
    user: Option[String],
    groupName: String,
    routeName: String
  ): ApiResponse[MonitorRouteSaveResult]

  def routeNames(groupName: String): ApiResponse[Seq[String]]

}
