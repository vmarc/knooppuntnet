package kpn.core.database.doc

import kpn.core.action.SystemStatus

case class SystemStatusDoc(_id: String, status: SystemStatus, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
