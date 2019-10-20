package kpn.core.db.couch

case class ViewResultRow(
  id: String,
  key: Seq[String],
  value: Seq[String]
)
