package kpn.core.database.doc

import kpn.core.action.SystemStatus

case class SystemStatusDoc(_id: String, status: SystemStatus, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
