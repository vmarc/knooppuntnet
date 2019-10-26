package kpn.core.database.doc

import kpn.shared.Timestamp

case class TimestampsDoc(_id: String, timestamps: Map[String, Timestamp], _rev: Option[String] = None)
