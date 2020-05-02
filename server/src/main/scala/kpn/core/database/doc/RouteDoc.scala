package kpn.core.database.doc

import kpn.api.common.route.RouteInfo

case class RouteDoc(_id: String, route: RouteInfo, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
