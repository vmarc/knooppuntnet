package kpn.api.common.location

case class LocationNode(
  name: String,
  nodeCount: Long,
  children: Seq[LocationNode]
)
