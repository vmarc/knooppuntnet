package kpn.api.common.route

case class RoutePath(
  id: Long,
  name: String, // forward, backward, start-tentacle-1, ...
  //  fromNodeId: Long,
  //  fromNodeName: String,
  //  toNodeId: Long,
  //  toNodeName: String,
  elementIds: Seq[Long]
)
