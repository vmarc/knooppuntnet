package kpn.core.database.doc

import kpn.api.common.changes.ChangeSetInfo

case class ChangeSetInfoDoc(_id: String, changeSetInfo: ChangeSetInfo, _rev: Option[String] = None) extends Doc
