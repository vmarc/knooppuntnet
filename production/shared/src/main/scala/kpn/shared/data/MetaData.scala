package kpn.shared.data

import kpn.shared.Timestamp

case class MetaData(version: Int, timestamp: Timestamp, changeSetId: Long) extends Meta
