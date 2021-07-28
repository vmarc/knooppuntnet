package kpn.core.database.doc

import kpn.api.common.ChangeSetSummary

case class ChangeSetSummaryDoc(_id: String, changeSetSummary: ChangeSetSummary, _rev: Option[String] = None) extends CouchDoc {
  def withRev(_newRev: Option[String]): CouchDoc = this.copy(_rev = _newRev)
}
