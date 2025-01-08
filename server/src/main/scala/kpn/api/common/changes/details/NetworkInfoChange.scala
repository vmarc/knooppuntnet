package kpn.api.common.changes.details

import kpn.api.base.WithStringId
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.RouteType
import kpn.api.common.common.ReferencedElements
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs

/*
  Describes the changes made to a given network in a given changeset.
 */
case class NetworkInfoChange(
  _id: String,
  key: ChangeKey,
  changeType: ChangeType,
  country: Option[Country],
  routeType: RouteType,
  networkId: Long,
  networkName: String,
  networkDataUpdate: Option[NetworkDataUpdate],
  nodeDiffs: RefDiffs,
  routeDiffs: RefDiffs,
  extraNodeDiffs: IdDiffs,
  extraWayDiffs: IdDiffs,
  extraRelationDiffs: IdDiffs,
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean
) extends WithStringId {

  def referencedElements: ReferencedElements = {
    ReferencedElements(nodeDiffs.ids.toSet, routeDiffs.ids.toSet)
  }
}
