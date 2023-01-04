package kpn.server.api.monitor.route

import kpn.api.base.ObjectId
import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.server.api.monitor.domain.MonitorGroup

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

  def analyzeAll(group: MonitorGroup, routeId: ObjectId): Unit

  def analyzeRelation(group: MonitorGroup, routeId: ObjectId, relationId: Long): Unit

}
