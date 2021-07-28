package kpn.core.database.doc

import kpn.api.common.changes.ChangeSetInfo

case class ChangeSetInfoDoc(_id: String, changeSetInfo: ChangeSetInfo, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
