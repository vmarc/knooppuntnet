package kpn.core.database.doc

case class DesignDoc(
  _id: String,
  _rev: Option[String],
  language: String,
  views: Map[String, ViewDoc] = Map.empty
) extends Doc
