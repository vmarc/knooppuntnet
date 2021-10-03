package kpn.api.common.changes.details

import kpn.api.base.WithStringId
import kpn.api.common.common.Ref
import kpn.api.common.data.MetaData
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.ChangeType
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
  before: Option[MetaData],
  after: Option[MetaData],
  connectionChanges: Seq[RefBooleanChange],
  roleConnectionChanges: Seq[RefBooleanChange],
  definedInNetworkChanges: Seq[RefBooleanChange],
  tagDiffs: Option[TagDiffs],
  nodeMoved: Option[NodeMoved],
  addedToRoute: Seq[Ref],
  removedFromRoute: Seq[Ref],
  addedToNetwork: Seq[Ref], // added to network relation (not included when only added to route within network)
  removedFromNetwork: Seq[Ref], // removed from network relation (not included when only removed to route within network)
  factDiffs: FactDiffs,
  facts: Seq[Fact],
  tiles: Seq[String],
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
