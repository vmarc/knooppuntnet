package kpn.core.database.doc

import kpn.server.api.monitor.domain.MonitorRouteChangeGeometry

case class MonitorRouteChangeGeometryDoc(_id: String, monitorRouteChangeGeometry: MonitorRouteChangeGeometry, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
