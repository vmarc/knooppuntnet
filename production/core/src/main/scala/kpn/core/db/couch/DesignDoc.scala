package kpn.core.db.couch

case class DesignDoc(
  _id: String,
  _rev: Option[String],
  language: String,
  views: Map[String, ViewDoc] = Map()
)
