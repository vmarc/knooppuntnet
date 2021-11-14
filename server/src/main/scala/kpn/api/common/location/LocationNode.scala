package kpn.api.common.location

case class LocationNode(
  name: String,
  localName: Option[String],
  nodeCount: Long,
  children: Seq[LocationNode]
)
