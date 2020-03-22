package kpn.core.action

import kpn.core.database.doc.Doc

case class ApiActionDoc(_id: String, api: ApiAction, _rev: Option[String] = None) extends Doc
