package kpn.core.database.doc

import kpn.core.action.LogAction

case class LogActionDoc(_id: String, log: LogAction, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
