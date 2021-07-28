package kpn.core.database.doc

import kpn.api.common.changes.details.RouteChange

case class RouteChangeDoc(_id: String, routeChange: RouteChange, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
