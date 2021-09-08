package kpn.api.common.route

case class RouteEdge(
  pathId: Long,
  sourceNodeId: Long,
  sinkNodeId: Long,
  meters: Long
)
