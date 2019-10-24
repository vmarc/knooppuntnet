package kpn.core.db

import kpn.shared.changes.ChangeSetInfo

case class ChangeSetInfoDoc(_id: String, changeSetInfo: ChangeSetInfo, _rev: Option[String] = None) extends Doc
