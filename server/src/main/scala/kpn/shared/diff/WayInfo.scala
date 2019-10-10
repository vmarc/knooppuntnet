package kpn.shared.diff

import kpn.shared.Timestamp
import kpn.shared.data.Meta
import kpn.shared.data.Tagable
import kpn.shared.data.Tags

case class WayInfo(
  id: Long,
  version: Int,
  changeSetId: Long,
  timestamp: Timestamp,
  tags: Tags
) extends Meta with Tagable
