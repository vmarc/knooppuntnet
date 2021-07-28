package kpn.core.database.doc

import kpn.api.common.NodeRoute

case class NodeRouteDoc(_id: String, nodeRoute: NodeRoute, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
