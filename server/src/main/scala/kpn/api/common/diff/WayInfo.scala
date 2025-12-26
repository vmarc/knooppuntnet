package kpn.api.common.diff

import kpn.api.common.data.Meta
import kpn.api.common.data.Tagable
import kpn.api.common.data.Way
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp
import kpn.core.doc.DetailWay

object WayInfo {
  def from(way: DetailWay): WayInfo = {
    WayInfo(
      way.id,
      way.version,
      way.changeSetId,
      way.timestamp,
      way.tags
    )
  }

  def oldFrom(way: Way): WayInfo = {
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
