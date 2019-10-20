package kpn.core.db

case class TileDoc(_id: String, title: String = "tile", _rev: Option[String] = None) extends Doc
