package kpn.core.db

import kpn.core.database.doc.CouchRouteDoc

case class RouteDocViewResultRow(
  key: String,
  id: Option[String],
  value: Option[ViewResultRowValue],
  error: Option[String],
  doc: Option[CouchRouteDoc]
)
