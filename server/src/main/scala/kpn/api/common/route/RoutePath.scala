package kpn.api.common.route

case class RoutePath(
  id: Long,
  name: String, // forward, backward, start-tentacle-1, ...
  elementIds: Seq[Long]
)
