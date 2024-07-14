package kpn.core.doc

case class RouteDetailPath(
  id: Long,
  name: String, // forward, backward, start-tentacle-1, ...
  elementIds: Seq[Long]
)
