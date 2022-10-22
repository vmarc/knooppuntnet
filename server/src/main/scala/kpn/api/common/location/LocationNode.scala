package kpn.api.common.location

case class LocationNode(
  name: String,
  nodeCount: Option[Long],
  children: Option[Seq[LocationNode]]
)
