package kpn.api.common

import kpn.core.database.doc.Doc

case class ChangeSetSummaryDoc(_id: String, changeSetSummary: ChangeSetSummary, _rev: Option[String] = None) extends Doc {
  def withRev(_newRev: Option[String]): Doc = this.copy(_rev = _newRev)
}
