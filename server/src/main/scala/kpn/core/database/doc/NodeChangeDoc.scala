package kpn.core.database.doc

import kpn.api.common.changes.details.NodeChange

case class NodeChangeDoc(_id: String, nodeChange: NodeChange, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
