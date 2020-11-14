package kpn.core.database.doc

import kpn.api.common.NodeRoute

case class NodeRouteDoc(_id: String, nodeRoute: NodeRoute, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
