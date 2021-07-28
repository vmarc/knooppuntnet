package kpn.core.database.doc

import kpn.server.api.monitor.domain.MonitorRouteChange

case class MonitorRouteChangeDoc(_id: String, monitorRouteChange: MonitorRouteChange, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
