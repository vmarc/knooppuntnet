package kpn.server.monitor.domain

import kpn.api.base.ObjectId

case class MonitorReferenceTileInfo(
  routeId: ObjectId,
  relationId: Option[Long],
  z: Long,
  x: Long,
  y: Long,
  lines: Seq[String]
)
