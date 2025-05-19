package kpn.api.common.diff

import kpn.api.common.data.Meta
import kpn.api.common.data.Tagable
import kpn.api.common.data.raw.RawWay
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

object WayInfo {
  def from(way: RawWay): WayInfo = {
    WayInfo(
      way.id,
      way.version,
      way.changeSetId,
      way.timestamp,
      way.tags
    )
  }
}

case class WayInfo(
  id: Long,
  version: Long,
  changeSetId: Long,
  timestamp: Timestamp,
  tags: Seq[Tag]
) extends Meta with Tagable
