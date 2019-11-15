package kpn.api.common.diff

import kpn.api.common.data.Meta
import kpn.api.common.data.Tagable
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp

case class WayInfo(
  id: Long,
  version: Long,
  changeSetId: Long,
  timestamp: Timestamp,
  tags: Tags
) extends Meta with Tagable
