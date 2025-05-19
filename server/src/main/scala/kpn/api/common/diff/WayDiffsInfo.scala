package kpn.api.common.diff

object WayDiffsInfo {
  def empty: WayDiffsInfo = {
    WayDiffsInfo(Seq.empty, Seq.empty, Seq.empty)
  }

  def from(wayDiffs: WayDiffs): WayDiffsInfo = {
    new WayDiffsInfo(
      wayDiffs.removed.map(WayInfo.from),
      wayDiffs.added.map(WayInfo.from),
      wayDiffs.updated
    )
  }
}

case class WayDiffsInfo(
  removed: Seq[WayInfo],
  added: Seq[WayInfo],
  updated: Seq[WayUpdate],
)
