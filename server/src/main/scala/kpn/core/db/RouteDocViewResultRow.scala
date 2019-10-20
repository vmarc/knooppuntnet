package kpn.core.db

case class RouteDocViewResultRow(
  key: String,
  id: Option[String],
  value: Option[ViewResultRowValue],
  error: Option[String],
  doc: Option[RouteDoc]
)
