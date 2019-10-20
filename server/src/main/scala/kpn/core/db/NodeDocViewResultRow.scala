package kpn.core.db

case class NodeDocViewResultRow(
  key: String,
  id: Option[String],
  value: Option[ViewResultRowValue],
  error: Option[String],
  doc: Option[NodeDoc]
)
