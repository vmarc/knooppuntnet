package kpn.core.database.doc

import kpn.api.common.monitor.MonitorRouteGroup

case class MonitorRouteGroupDoc(_id: String, routeGroup: MonitorRouteGroup, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
