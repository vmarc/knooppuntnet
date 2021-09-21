package kpn.core.database.doc

import kpn.core.mongo.doc.RouteDoc

case class CouchRouteDoc(_id: String, route: RouteDoc, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
