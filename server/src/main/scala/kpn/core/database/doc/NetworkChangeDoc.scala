package kpn.core.database.doc

import kpn.api.common.changes.details.NetworkInfoChange

case class NetworkChangeDoc(_id: String, networkChange: NetworkInfoChange, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
