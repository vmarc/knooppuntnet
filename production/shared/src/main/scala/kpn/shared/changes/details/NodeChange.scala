package kpn.shared.changes.details

import kpn.shared.Fact
import kpn.shared.Subset
import kpn.shared.common.Ref
import kpn.shared.common.ToStringBuilder
import kpn.shared.data.raw.RawNode
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.common.FactDiffs
import kpn.shared.diff.node.NodeMoved

/*
  Describes the changes made to a given network node in a given changeset.
 */
case class NodeChange(
  key: ChangeKey,
  changeType: ChangeType,
  subsets: Seq[Subset],
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
  facts: Seq[Fact]
) {

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

  def happy: Boolean = {
    changeType != ChangeType.InitialValue && (
      changeType == ChangeType.Create ||
        definedInNetworkChanges.exists(_.after) ||
        addedToRoute.nonEmpty ||
        addedToNetwork.nonEmpty ||
        factDiffs.happy ||
        facts.contains(Fact.Added) ||
        (facts.contains(Fact.WasOrphan) && !(facts.contains(Fact.Deleted) || facts.contains(Fact.LostHikingNodeTag) || facts.contains(Fact.LostBicycleNodeTag))) ||
        facts.contains(Fact.WasIgnored)
      )
  }

  def investigate: Boolean = {
    changeType != ChangeType.InitialValue && (
      changeType == ChangeType.Delete ||
        definedInNetworkChanges.exists(_.after == false) ||
        removedFromRoute.nonEmpty ||
        removedFromNetwork.nonEmpty ||
        factDiffs.investigate ||
        facts.contains(Fact.Deleted) ||
        facts.contains(Fact.BecomeIgnored) ||
        facts.contains(Fact.BecomeOrphan) ||
        facts.contains(Fact.LostHikingNodeTag) ||
        facts.contains(Fact.LostBicycleNodeTag)
      )
  }

  def toRef: Ref = Ref(id, name)

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("key", key).
    field("changeType", changeType).
    field("subsets", subsets).
    field("name", name).
    field("before", before).
    field("after", after).
    field("connectionChanges", connectionChanges).
    field("roleConnectionChanges", roleConnectionChanges).
    field("definedInNetworkChanges", definedInNetworkChanges).
    field("tagDiffs", tagDiffs).
    field("nodeMoved", nodeMoved).
    field("addedToRoute", addedToRoute).
    field("removedFromRoute", removedFromRoute).
    field("addedToNetwork", addedToNetwork).
    field("removedFromNetwork", removedFromNetwork).
    field("factDiffs", factDiffs).
    field("facts", facts).
    field("happy", happy).
    field("investigate", investigate).
    build
}
