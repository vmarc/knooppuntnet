package kpn.shared.node

import kpn.shared.Fact
import kpn.shared.changes.details.ChangeKey
import kpn.shared.changes.details.RefBooleanChange
import kpn.shared.common.Ref
import kpn.shared.data.MetaData
import kpn.shared.data.Tags
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.common.FactDiffs
import kpn.shared.diff.node.NodeMoved

case class NodeChangeInfo(
  id: Long,
  version: Option[Int],
  changeKey: ChangeKey,
  changeTags: Tags,
  comment: Option[String],
  before: Option[MetaData],
  after: Option[MetaData],
  connectionChanges: Seq[RefBooleanChange],
  definedInNetworkChanges: Seq[RefBooleanChange],
  tagDiffs: Option[TagDiffs],
  nodeMoved: Option[NodeMoved],
  addedToRoute: Seq[Ref],
  removedFromRoute: Seq[Ref],
  addedToNetwork: Seq[Ref],
  removedFromNetwork: Seq[Ref],
  factDiffs: FactDiffs,
  facts: Seq[Fact],
  happy: Boolean,
  investigate: Boolean
)
