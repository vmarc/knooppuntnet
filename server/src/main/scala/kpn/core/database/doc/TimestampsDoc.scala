package kpn.core.database.doc

import kpn.api.custom.Timestamp

case class TimestampsDoc(_id: String, timestamps: Map[String, Timestamp], _rev: Option[String] = None)
