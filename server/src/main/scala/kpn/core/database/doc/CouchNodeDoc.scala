package kpn.core.database.doc

import kpn.api.common.NodeInfo

case class CouchNodeDoc(_id: String, node: NodeInfo, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
