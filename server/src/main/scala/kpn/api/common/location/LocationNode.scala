package kpn.api.common.location

case class LocationNode(
  name: String,
  nodeCount: Long,
  routeCount: Long,
  factCount: Long,
  children: Option[Seq[LocationNode]]
)
