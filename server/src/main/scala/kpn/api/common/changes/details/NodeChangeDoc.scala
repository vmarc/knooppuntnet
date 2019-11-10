package kpn.api.common.changes.details

import kpn.core.database.doc.Doc

case class NodeChangeDoc(_id: String, nodeChange: NodeChange, _rev: Option[String] = None) extends Doc
