package kpn.api.common.changes.details

import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.common.common.ReferencedElements
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.id.WithStringId

case class NetworkChange(
  _id: String,
  key: ChangeKey,
  networkId: Long,
  networkName: Option[String],
  changeType: ChangeType,
  country: Option[Country],
  routeType: RouteType,
  networkDataUpdate: Option[NetworkDataUpdate],
  nodes: IdDiffs,
  ways: IdDiffs,
  relations: IdDiffs,
  nodeDiffs: RefDiffs,
  routeDiffs: RefDiffs,
  extraNodeDiffs: IdDiffs,
  extraWayDiffs: IdDiffs,
  extraRelationDiffs: IdDiffs,
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean
) extends WithStringId {

  def impactedNodeIds: Seq[Long] = {
    nodeDiffs.ids
  }

  def impactedRelationIds: Seq[Long] = {
    relations.ids
  }

  def referencedElements: ReferencedElements = {
    ReferencedElements(nodeDiffs.ids.toSet, routeDiffs.ids.toSet)
  }
}
