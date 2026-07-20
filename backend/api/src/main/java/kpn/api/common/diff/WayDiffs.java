package kpn.api.common.diff;

import kpn.api.common.data.raw.RawWay;
import kpn.api.common.diff.WayUpdate;

import com.google.common.collect.ImmutableList;

public record WayDiffs(
  ImmutableList<RawWay> removed,
  ImmutableList<RawWay> added,
  ImmutableList<WayUpdate> updated
) {}

/* TODO migrate
package kpn.api.common.diff

import kpn.api.common.data.raw.RawWay

object WayDiffs {
  def empty: WayDiffs = WayDiffs()
}

case class WayDiffs(
  removed: Seq[RawWay] = Seq.empty,
  added: Seq[RawWay] = Seq.empty,
  updated: Seq[WayUpdate] = Seq.empty
) {
  def isEmpty: Boolean = removed.isEmpty && added.isEmpty && updated.isEmpty

  def nonEmpty: Boolean = removed.nonEmpty || added.nonEmpty || updated.nonEmpty
}

*/
