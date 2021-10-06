package kpn.core.database.doc

import kpn.api.base.WithStringId
import kpn.core.action.LogAction

case class LogActionDoc(_id: String, log: LogAction, _rev: Option[String] = None) extends CouchDoc with WithStringId {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
