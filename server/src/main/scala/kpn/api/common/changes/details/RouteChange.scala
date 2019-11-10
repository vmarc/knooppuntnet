package kpn.api.common.changes.details

import kpn.api.common.common.Ref
import kpn.api.common.common.ReferencedElements
import kpn.api.common.common.ToStringBuilder
import kpn.api.common.data.raw.RawWay
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.WayUpdate
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Fact
import kpn.api.custom.Subset

/*
  Describes the changes made to a given route in a given changeset.
 */
case class RouteChange(
  key: ChangeKey,
  changeType: ChangeType,
  name: String,
  addedToNetwork: Seq[Ref],
  removedFromNetwork: Seq[Ref],
  before: Option[RouteData],
  after: Option[RouteData],
  removedWays: Seq[RawWay],
  addedWays: Seq[RawWay],
  updatedWays: Seq[WayUpdate],
  diffs: RouteDiff,
  facts: Seq[Fact],
  happy: Boolean = false,
  investigate: Boolean = false
) {

  def id: Long = key.elementId

  def subsets: Seq[Subset] = {
    (before.toSeq.flatMap(_.subset) ++ after.toSeq.flatMap(_.subset)).distinct.sorted
  }

  def toRef: Ref = Ref(id, name)

  def isEmpty: Boolean = addedToNetwork.isEmpty &&
    removedFromNetwork.isEmpty &&
    removedWays.isEmpty &&
    addedWays.isEmpty &&
    updatedWays.isEmpty &&
    diffs.isEmpty &&
    facts.isEmpty

  def referencedElements: ReferencedElements = {
    ReferencedElements(nodeIds = nodeIdsReferencedIn(before) ++ nodeIdsReferencedIn(after), Set(id))
  }

  private def nodeIdsReferencedIn(routeData: Option[RouteData]): Set[Long] = {
    routeData.toSeq.flatMap(_.networkNodes.map(_.id)).toSet
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("key", key).
    field("changeType", changeType).
    field("name", name).
    field("addedToNetwork", addedToNetwork).
    field("removedFromNetwork", removedFromNetwork).
    field("before", before).
    field("after", after).
    field("removedWays", removedWays).
    field("addedWays", addedWays).
    field("updatedWays", updatedWays).
    field("diffs", diffs).
    field("facts", facts).
    field("happy", happy).
    field("investigate", investigate).
    build
}
