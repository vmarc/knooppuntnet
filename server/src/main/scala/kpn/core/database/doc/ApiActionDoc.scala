package kpn.core.database.doc

import kpn.core.action.ApiAction

case class ApiActionDoc(_id: String, api: ApiAction, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
