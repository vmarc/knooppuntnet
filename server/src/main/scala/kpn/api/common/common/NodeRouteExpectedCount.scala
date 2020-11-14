package kpn.api.common.common

case class NodeRouteExpectedCount(
  nodeId: Long,
  nodeName: String,
  locationNames: Seq[String],
  routeCount: Int
)
