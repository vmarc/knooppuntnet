package kpn.server.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.ApiResponse
import kpn.api.custom.Day
import kpn.server.monitor.domain.MonitorGroup

import scala.xml.Elem

trait MonitorUpdater {

  def add(
    user: String,
    groupName: String,
    properties: MonitorRouteProperties
  ): MonitorRouteSaveResult

  def update(
    user: String,
    groupName: String,
    routeName: String,
    properties: MonitorRouteProperties
  ): MonitorRouteSaveResult

  def upload(
    user: String,
    groupName: String,
    routeName: String,
    relationId: Long,
    referenceDay: Day,
    filename: String,
    xml: Elem
  ): MonitorRouteSaveResult

  def analyze(groupName: String, routeName: String): ApiResponse[MonitorRouteSaveResult]

  def analyzeAll(group: MonitorGroup, routeId: ObjectId): Unit

  def analyzeRelation(group: MonitorGroup, routeId: ObjectId, relationId: Long): Unit

  def resetSubRelationGpxReference(groupName: String, routeName: String, subRelationId: Long): Unit

}
