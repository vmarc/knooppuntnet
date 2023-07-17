package kpn.server.monitor.repository

case class MonitorRouteStateSummary(
  relationId: Long,
  deviationDistance: Long,
  deviationCount: Long,
  osmWayCount: Long,
  osmDistance: Long,
  osmSegmentCount: Long,
  startNodeId: Option[Long],
  endNodeId: Option[Long],
  happy: Boolean,
)
