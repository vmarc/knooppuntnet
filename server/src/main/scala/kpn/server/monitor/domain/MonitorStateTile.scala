package kpn.server.monitor.domain

import kpn.core.doc.WithObjectId
import org.bson.types.ObjectId

case class MonitorStateTile(
  _id: ObjectId,
  routeId: ObjectId,
  relationId: Long,
  z: Long,
  x: Long,
  y: Long,
  deviations: Seq[MonitorStateTileDeviation],
  matchesLines: Seq[String],
  segments: Seq[MonitorSegment],
) extends WithObjectId {

  def key: String = {
    s"$relationId-$z-$x-$y"
  }
}
