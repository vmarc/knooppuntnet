package kpn.core.database.doc

import kpn.api.common.changes.details.RouteChange

case class RouteChangeDoc(_id: String, routeChange: RouteChange, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
