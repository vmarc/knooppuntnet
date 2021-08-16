package kpn.api.common.changes.details

import kpn.api.base.WithStringId
import kpn.api.common.common.ReferencedElements
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.NetworkType

/*
  Describes the changes made to a given network in a given changeset.
 */
case class NetworkInfoChange(
  _id: String,
  key: ChangeKey,
  changeType: ChangeType,
  country: Option[Country],
  networkType: NetworkType,
  networkId: Long,
  networkName: String,
  orphanRouteDiffs: RefChanges,
  orphanNodeDiffs: RefChanges,
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

    val nodeIds: Set[Long] = (
      orphanNodeDiffs.oldRefs.map(_.id) ++
        orphanNodeDiffs.newRefs.map(_.id) ++
        nodeDiffs.ids
      ).toSet

    val routeIds: Set[Long] = (
      orphanRouteDiffs.oldRefs.map(_.id) ++
        orphanRouteDiffs.newRefs.map(_.id) ++
        routeDiffs.ids
      ).toSet

    ReferencedElements(nodeIds, routeIds)
  }
}
