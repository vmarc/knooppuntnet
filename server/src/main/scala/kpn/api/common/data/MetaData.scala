package kpn.api.common.data

import kpn.api.custom.Timestamp

case class MetaData(version: Int, timestamp: Timestamp, changeSetId: Long) extends Meta
