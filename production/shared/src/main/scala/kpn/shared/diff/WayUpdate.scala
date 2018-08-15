package kpn.shared.diff

import kpn.shared.data.MetaData
import kpn.shared.data.raw.RawNode

case class WayUpdate(
  id: Long,
  before: MetaData,
  after: MetaData,
  removedNodes: Seq[RawNode] = Seq(),
  addedNodes: Seq[RawNode] = Seq(),
  updatedNodes: Seq[NodeUpdate] = Seq(),
  directionReversed: Boolean = false,
  tagDiffs: Option[TagDiffs] = None
) {

  def isNewVersion: Boolean = before.version != after.version

}
