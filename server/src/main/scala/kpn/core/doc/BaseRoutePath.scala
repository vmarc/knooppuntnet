package kpn.core.doc

case class BaseRoutePath(
  id: Long,
  name: String, // forward, backward, start-tentacle-1, ...
  elementIds: Seq[Long]
) extends Storable
