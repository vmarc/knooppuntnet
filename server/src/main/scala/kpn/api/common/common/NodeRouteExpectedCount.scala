package kpn.api.common.common

case class NodeRouteExpectedCount(
  nodeId: Long,
  routeCount: Int,
  locationNames: Seq[String]
)
