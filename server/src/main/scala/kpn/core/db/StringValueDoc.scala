package kpn.core.db

case class StringValueDoc(_id: String, value: String, _rev: Option[String] = None) extends Doc
