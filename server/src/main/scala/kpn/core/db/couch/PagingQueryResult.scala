package kpn.core.db.couch

import spray.json.JsValue

case class PagingQueryResult(
  totalRows: Int,
  offset: Int,
  rows: Seq[JsValue]
)
