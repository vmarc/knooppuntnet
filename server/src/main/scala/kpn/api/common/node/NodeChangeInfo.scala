package kpn.api.common.node

import kpn.api.common.LatLonImpl
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.ChangeType
import kpn.api.custom.Fact
import kpn.api.custom.Tags

case class NodeChangeInfo(
  rowIndex: Long,
  id: Long,
  version: Option[Long],
  changeKey: ChangeKey,
  changeType: ChangeType,
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
  factDiffs: Option[FactDiffs],
  facts: Seq[Fact],
  initialTags: Option[Tags],
  initialLatLon: Option[LatLonImpl],
  happy: Boolean,
  investigate: Boolean
)
