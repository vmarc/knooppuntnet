package kpn.core.database.doc

import kpn.server.analyzer.engine.changes.changes.RouteElements

case class RouteElementsDoc(_id: String, routeElements: RouteElements, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}

