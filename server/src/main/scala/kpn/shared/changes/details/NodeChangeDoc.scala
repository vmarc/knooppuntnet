package kpn.shared.changes.details

import kpn.core.db.Doc

case class NodeChangeDoc(_id: String, nodeChange: NodeChange, _rev: Option[String] = None) extends Doc
