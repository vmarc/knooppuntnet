package kpn.api.common.diff;

import kpn.api.common.diff.WayInfo;
import kpn.api.common.diff.WayUpdate;

import com.google.common.collect.ImmutableList;

public record WayDiffsInfo(
  ImmutableList<WayInfo> removed,
  ImmutableList<WayInfo> added,
  ImmutableList<WayUpdate> updated
) {}

/* TODO migrate
package kpn.api.common.diff

object WayDiffsInfo {
  def empty: WayDiffsInfo = {
    WayDiffsInfo(Seq.empty, Seq.empty, Seq.empty)
  }
}

case class WayDiffsInfo(
  removed: Seq[WayInfo] = Seq.empty,
  added: Seq[WayInfo] = Seq.empty,
  updated: Seq[WayUpdate] = Seq.empty,
) {
  def isEmpty: Boolean = removed.isEmpty && added.isEmpty && updated.isEmpty

  def nonEmpty: Boolean = removed.nonEmpty || added.nonEmpty || updated.nonEmpty
}

*/
