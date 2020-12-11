package kpn.core.database.doc

import kpn.api.common.changes.details.NetworkChange

case class NetworkChangeDoc(_id: String, networkChange: NetworkChange, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
