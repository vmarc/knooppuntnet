package kpn.core.database.doc

case class TileDoc(_id: String, title: String = "tile", _rev: Option[String] = None) extends Doc
