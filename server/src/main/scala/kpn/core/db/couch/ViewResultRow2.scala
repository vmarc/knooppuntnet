package kpn.core.db.couch

import kpn.core.db.ViewResultRowValue

case class ViewResultRow2(
  key: String,
  id: Option[String],
  value: Option[ViewResultRowValue],
  error: Option[String]
)
