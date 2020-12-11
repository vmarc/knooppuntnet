package kpn.core.database.doc

import kpn.core.action.UpdateAction

case class UpdateActionDoc(_id: String, update: UpdateAction, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
