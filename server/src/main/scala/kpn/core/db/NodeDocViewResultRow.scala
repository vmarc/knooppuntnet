package kpn.core.db

import kpn.core.database.doc.NodeDoc

case class NodeDocViewResultRow(
  key: String,
  id: Option[String],
  value: Option[ViewResultRowValue],
  error: Option[String],
  doc: Option[NodeDoc]
)
