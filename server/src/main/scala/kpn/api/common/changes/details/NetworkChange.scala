package kpn.api.common.changes.details

import kpn.api.common.common.ReferencedElements
import kpn.api.common.common.ToStringBuilder
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.NetworkType

/*
  Describes the changes made to a given network in a given changeset.
 */
case class NetworkChange(
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
  nodes: IdDiffs,
  ways: IdDiffs,
  relations: IdDiffs,
  happy: Boolean,
  investigate: Boolean,
  impact: Boolean
) {

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

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("_id", _id).
    field("key", key).
    field("changeType", changeType).
    field("orphanRoutes", orphanRoutes).
    field("orphanNodes", orphanNodes).
    field("country", country).
    field("networkType", networkType).
    field("networkId", networkId).
    field("networkName", networkName).
    field("networkDataUpdate", networkDataUpdate).
    field("networkNodes", networkNodes).
    field("routes", routes).
    field("nodes", nodes).
    field("ways", ways).
    field("relations", relations).
    field("happy", happy).
    field("investigate", investigate).
    field("impact", impact).
    build
}
