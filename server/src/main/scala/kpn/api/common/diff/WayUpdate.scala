package kpn.api.common.diff

import kpn.api.common.data.MetaData
import kpn.api.common.data.Node

case class WayUpdate(
  id: Long,
  before: MetaData,
  after: MetaData,
  removedNodes: Seq[Node] = Seq.empty,
  addedNodes: Seq[Node] = Seq.empty,
  updatedNodes: Seq[NodeUpdate] = Seq.empty,
  directionReversed: Boolean = false,
  tagDiffs: Option[TagDiffs] = None
)
