package kpn.core.database.doc

import kpn.server.api.monitor.domain.MonitorRouteState

case class MonitorRouteStateDoc(_id: String, monitorRouteState: MonitorRouteState, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
