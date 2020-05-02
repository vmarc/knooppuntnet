package kpn.api.common.changes.details

import kpn.core.database.doc.Doc

case class NetworkChangeDoc(_id: String, networkChange: NetworkChange, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
