package kpn.core.doc

import kpn.api.id.Storable

case class SuperSegment(
  segments: Seq[SuperSubSegment]
) extends Storable
