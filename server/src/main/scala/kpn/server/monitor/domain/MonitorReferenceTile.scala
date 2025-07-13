package kpn.server.monitor.domain

import kpn.api.base.ObjectId

case class MonitorReferenceTile(
  _id: ObjectId,
  routeId: ObjectId,
  relationId: Option[Long],
  z: Long,
  x: Long,
  y: Long,
  lines: Seq[String]
)
