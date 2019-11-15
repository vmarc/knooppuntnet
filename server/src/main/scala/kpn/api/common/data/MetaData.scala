package kpn.api.common.data

import kpn.api.custom.Timestamp

case class MetaData(version: Long, timestamp: Timestamp, changeSetId: Long) extends Meta
