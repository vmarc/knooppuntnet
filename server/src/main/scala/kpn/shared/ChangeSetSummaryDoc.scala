package kpn.shared

import kpn.core.db.Doc

case class ChangeSetSummaryDoc(_id: String, changeSetSummary: ChangeSetSummary, _rev: Option[String] = None) extends Doc
