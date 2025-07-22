package kpn.api.common.route

import kpn.api.common.Bounds

case class SuperSegment(
  id: Long,
  bounds: Option[Bounds],
  segments: Seq[SuperSubSegment]
)
