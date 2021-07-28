package kpn.core.database.doc

import kpn.api.common.changes.details.NodeChange

case class NodeChangeDoc(_id: String, nodeChange: NodeChange, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
