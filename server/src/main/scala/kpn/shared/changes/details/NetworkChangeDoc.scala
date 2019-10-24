package kpn.shared.changes.details

import kpn.core.db.Doc

case class NetworkChangeDoc(_id: String, networkChange: NetworkChange, _rev: Option[String] = None) extends Doc
