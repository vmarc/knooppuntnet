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
  orphanRoutes: RefChanges,
  orphanNodes: RefChanges,
  networkDataUpdate: Option[NetworkDataUpdate],
  networkNodes: RefDiffs,
  routes: RefDiffs,
  extraNodes: IdDiffs,
  extraWays: IdDiffs,
  extraRelations: IdDiffs,
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean
) extends WithStringId {

  def referencedElements: ReferencedElements = {

    val nodeIds: Set[Long] = (
      orphanNodes.oldRefs.map(_.id) ++
        orphanNodes.newRefs.map(_.id) ++
        networkNodes.ids
      ).toSet

    val routeIds: Set[Long] = (
      orphanRoutes.oldRefs.map(_.id) ++
        orphanRoutes.newRefs.map(_.id) ++
        routes.ids
      ).toSet

    ReferencedElements(nodeIds, routeIds)
  }
}
