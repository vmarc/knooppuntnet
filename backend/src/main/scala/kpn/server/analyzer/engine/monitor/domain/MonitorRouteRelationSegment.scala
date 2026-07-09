package kpn.server.analyzer.engine.monitor.domain

import kpn.api.common.monitor.MonitorRouteSegment

object MonitorRouteRelationSegment {

  def apply(
    relationId: Long,
    segment: MonitorRouteSegment,
  ): MonitorRouteRelationSegment = {
    MonitorRouteRelationSegment(
      s"$relationId-${segment.id}",
      relationId,
      segment,
    )
  }
}

case class MonitorRouteRelationSegment(
  id: String,
  relationId: Long,
  segment: MonitorRouteSegment,
) {

  def startNodeId: Long = segment.startNodeId

  def endNodeId: Long = segment.endNodeId
}
