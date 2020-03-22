package kpn.core.action

import kpn.core.database.doc.Doc

case class UpdateActionDoc(_id: String, update: UpdateAction, _rev: Option[String] = None) extends Doc
