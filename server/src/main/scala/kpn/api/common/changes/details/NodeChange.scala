package kpn.api.common.changes.details

import kpn.api.base.WithStringId
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawNode
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.common.location.Location
import kpn.api.custom.Fact
import kpn.api.custom.Subset

/*
  Describes the changes made to a given network node in a given changeset.
 */
case class NodeChange(
  _id: String,
  key: ChangeKey,
  changeType: ChangeType,
  subsets: Seq[Subset],
  locations: Seq[String],
  name: String,
  before: Option[RawNode],
  after: Option[RawNode],
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
  // following values are filled in by NodeChangeAnalyzer.analyzed
  happy: Boolean = false,
  investigate: Boolean = false,
  impact: Boolean = false,
  locationHappy: Boolean = false,
  locationInvestigate: Boolean = false,
  locationImpact: Boolean = false,
  comment: Option[String] = None
) extends WithStringId {

  def id: Long = key.elementId

  def isEmpty: Boolean = {
    connectionChanges.isEmpty &&
      roleConnectionChanges.isEmpty &&
      definedInNetworkChanges.isEmpty &&
      tagDiffs.isEmpty &&
      nodeMoved.isEmpty &&
      addedToRoute.isEmpty &&
      removedFromRoute.isEmpty &&
      addedToNetwork.isEmpty &&
      removedFromNetwork.isEmpty &&
      factDiffs.isEmpty &&
      facts.isEmpty
  }

  def toRef: Ref = Ref(id, name)
}
