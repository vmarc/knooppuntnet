package kpn.core.database.doc

import kpn.api.common.changes.details.NetworkChange

case class NetworkChangeDoc(_id: String, networkChange: NetworkChange, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
