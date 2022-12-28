package kpn.server.api.monitor.route

import kpn.api.common.monitor.MonitorRouteProperties
import kpn.api.common.monitor.MonitorRouteSaveResult
import kpn.api.custom.Day
import kpn.server.api.monitor.domain.MonitorRoute

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

}
