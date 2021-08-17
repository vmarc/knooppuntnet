package kpn.api.common.changes

import kpn.api.common.ChangeSetSummary
import kpn.api.common.LocationChangeSetSummary
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.ReferencedElements

case class ChangeSetData(
  summary: ChangeSetSummary,
  locationSummary: LocationChangeSetSummary,
  networkChanges: Seq[NetworkInfoChange],
  routeChanges: Seq[RouteChange],
  nodeChanges: Seq[NodeChange]
) {

  def changeSetId: Long = summary.key.changeSetId

  def happy: Boolean = networkChanges.exists(_.happy)

  def investigate: Boolean = networkChanges.exists(_.investigate)

  def noImpact: Boolean = !(happy || investigate)

  def referencedElements: ReferencedElements = {
    val es1 = networkChanges.map(_.referencedElements)
    val es2 = routeChanges.map(_.referencedElements)
    val e3 = ReferencedElements(nodeIds = nodeChanges.map(_.id).toSet)
    val e = es1 ++ es2 :+ e3
    ReferencedElements.merge(e: _*)
  }
}
