package kpn.api.common.changes.details

import kpn.api.common.common.Ref
import kpn.api.common.common.ToStringBuilder
import kpn.api.common.data.raw.RawNode
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.Fact
import kpn.api.custom.Subset

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
  facts: Seq[Fact],
  happy: Boolean = false,
  investigate: Boolean = false
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
