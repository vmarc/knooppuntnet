package kpn.api.common.monitor

case class MonitorRouteSubRelation(
  subRelationIndex: Option[Long],
  relationId: Long,
  name: String,
  wayCount: Long
)
