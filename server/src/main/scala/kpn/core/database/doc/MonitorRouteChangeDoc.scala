package kpn.core.database.doc

import kpn.api.common.monitor.MonitorRouteChange

case class MonitorRouteChangeDoc(_id: String, monitorRouteChange: MonitorRouteChange, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
