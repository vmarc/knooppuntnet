package kpn.shared.changes.details

import kpn.core.database.doc.Doc

case class NetworkChangeDoc(_id: String, networkChange: NetworkChange, _rev: Option[String] = None) extends Doc
