package kpn.server.monitor.domain

import kpn.core.doc.Storable
import org.bson.types.ObjectId

case class MonitorReferenceTileInfo(
  routeId: ObjectId,
  relationId: Option[Long],
  z: Long,
  x: Long,
  y: Long,
  lines: Seq[String]
) extends Storable
