package kpn.core.database.doc

import kpn.api.custom.Timestamp

case class TimestampDoc(_id: String, value: Timestamp, _rev: Option[String] = None) extends Doc
