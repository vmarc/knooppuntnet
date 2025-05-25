package kpn.api.common.changes.details

import kpn.api.base.WithStringId
import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.RouteLocationAnalysis
import kpn.api.common.common.Ref
import kpn.api.common.common.ReferencedElements
import kpn.api.common.diff.RouteData
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.route.RouteNodeChange
import kpn.api.custom.Subset

/*
  Describes the changes made to a given route in a given changeset.
 */
case class RouteChange(
  _id: String,
  key: ChangeKey,
  changeType: ChangeType,
  name: String,
  locationAnalysis: RouteLocationAnalysis,
  addedToNetwork: Seq[Ref],
  removedFromNetwork: Seq[Ref],
  before: Option[RouteData],
  after: Option[RouteData],
  diffs: RouteDiff,
  nodeChanges: Seq[RouteNodeChange],
  facts: Seq[Fact],
  // following values are filled in by RouteChangeAnalyzer.analyzed
  happy: Boolean = false,
  investigate: Boolean = false,
  impact: Boolean = false,
  locationHappy: Boolean = false,
  locationInvestigate: Boolean = false,
  locationImpact: Boolean = false
) extends WithStringId {

  def id: Long = key.elementId

  def subsets: Seq[Subset] = {
    (before.toSeq.flatMap(_.subsets) ++ after.toSeq.flatMap(_.subsets)).distinct.sorted
  }

  def toRef: Ref = Ref(id, name)

  def isEmpty: Boolean = addedToNetwork.isEmpty &&
    removedFromNetwork.isEmpty &&
    diffs.isEmpty &&
    facts.isEmpty

  def referencedElements: ReferencedElements = {
    ReferencedElements(nodeIds = nodeIdsReferencedIn(before) ++ nodeIdsReferencedIn(after), Set(id))
  }

  private def nodeIdsReferencedIn(routeData: Option[RouteData]): Set[Long] = {
    routeData.toSeq.flatMap(_.networkNodes.map(_.nodeId)).toSet
  }
}
