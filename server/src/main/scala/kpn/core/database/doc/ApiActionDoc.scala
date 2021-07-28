package kpn.core.database.doc

import kpn.core.action.ApiAction

case class ApiActionDoc(_id: String, api: ApiAction, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
