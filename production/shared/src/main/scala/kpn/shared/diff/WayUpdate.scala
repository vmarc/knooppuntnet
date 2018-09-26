package kpn.shared.diff

import kpn.shared.data.MetaData
import kpn.shared.data.raw.RawNode

case class WayUpdate(
  id: Long,
  before: MetaData,
  after: MetaData,
  removedNodes: Seq[RawNode] = Seq.empty,
  addedNodes: Seq[RawNode] = Seq.empty,
  updatedNodes: Seq[NodeUpdate] = Seq.empty,
  directionReversed: Boolean = false,
  tagDiffs: Option[TagDiffs] = None
) {

  def isNewVersion: Boolean = before.version != after.version

}
