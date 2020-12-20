package kpn.core.database.doc

import kpn.server.api.monitor.domain.MonitorRoute

case class MonitorRouteDoc(_id: String, monitorRoute: MonitorRoute, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
