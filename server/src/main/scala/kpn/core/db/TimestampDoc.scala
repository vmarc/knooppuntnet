package kpn.core.db

import kpn.shared.Timestamp

case class TimestampDoc(_id: String, value: Timestamp, _rev: Option[String] = None)
