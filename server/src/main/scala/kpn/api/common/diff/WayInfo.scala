package kpn.api.common.diff

import kpn.api.common.data.Meta
import kpn.api.common.data.Tagable
import kpn.api.custom.Tag
import kpn.api.custom.Timestamp

case class WayInfo(
  id: Long,
  version: Long,
  changeSetId: Long,
  timestamp: Timestamp,
  tags: Seq[Tag]
) extends Meta with Tagable
