package kpn.api.common.diff

import kpn.api.common.data.raw.RawWay

case class WayDiffs(
  removed: Seq[RawWay] = Seq.empty,
  added: Seq[RawWay] = Seq.empty,
  updated: Seq[WayUpdate] = Seq.empty
) {

  def nonEmpty: Boolean = removed.nonEmpty || added.nonEmpty || updated.nonEmpty
}
