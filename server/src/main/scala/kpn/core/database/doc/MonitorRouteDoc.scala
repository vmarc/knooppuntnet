package kpn.core.database.doc

import kpn.server.api.monitor.domain.MonitorRoute

case class MonitorRouteDoc(_id: String, monitorRoute: MonitorRoute, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
