package kpn.api.common.diff

import kpn.api.common.data.MetaData

case class WayUpdate(
  id: Long,
  before: MetaData,
  after: MetaData,
  removedNodeIds: Seq[Long],
  addedNodeIds: Seq[Long],
  directionReversed: Boolean,
  tagDiffs: Option[TagDiffs]
)
