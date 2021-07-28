package kpn.core.database.doc

import kpn.server.analyzer.engine.changes.changes.RouteElements

case class RouteElementsDoc(_id: String, routeElements: RouteElements, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}

