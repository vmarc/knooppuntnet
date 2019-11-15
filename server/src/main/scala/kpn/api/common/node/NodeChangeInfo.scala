package kpn.api.common.node

import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.Fact
import kpn.api.custom.Tags

case class NodeChangeInfo(
  id: Long,
  version: Option[Long],
  changeKey: ChangeKey,
  changeTags: Tags,
  comment: Option[String],
  before: Option[MetaData],
  after: Option[MetaData],
  connectionChanges: Seq[RefBooleanChange],
  roleConnectionChanges: Seq[RefBooleanChange],
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
