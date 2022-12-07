package kpn.server.api.monitor.domain

case class MonitorRouteRelation(
  relationId: Long,
  name: String,
  deviationDistance: Long,
  deviationCount: Long,
  osmWayCount: Long,
  osmDistance: Long,
  osmSegmentCount: Long,
  happy: Boolean,
  relations: Seq[MonitorRouteRelation] // further sub-relations
)
