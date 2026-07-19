package kpn.server.monitor.route.update

case class MonitorRouteRelationGapInfo(
  relationId: Long,
  osmSegmentCount: Long,
  startNodeId: Long,
  endNodeId: Long,
  gaps: Option[String]
)