package kpn.core.database.doc

import kpn.api.common.monitor.MonitorRoute

case class MonitorRouteDoc(_id: String, monitorRoute: MonitorRoute, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
