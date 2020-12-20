package kpn.core.database.doc

import kpn.server.api.monitor.domain.MonitorRouteReference

case class MonitorRouteReferenceDoc(_id: String, monitorRouteReference: MonitorRouteReference, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
