package kpn.shared.changes.details

import kpn.shared.Country
import kpn.shared.NetworkType
import kpn.shared.common.ReferencedElements
import kpn.shared.common.ToStringBuilder
import kpn.shared.diff.IdDiffs
import kpn.shared.diff.NetworkDataUpdate
import kpn.shared.diff.RefDiffs

/*
  Describes the changes made to a given network in a given changeset.
 */
case class NetworkChange(
  key: ChangeKey,
  changeType: ChangeType,
  country: Option[Country],
  networkType: NetworkType,
  networkId: Long,
  networkName: String,
  orphanRoutes: RefChanges,
  ignoredRoutes: RefChanges,
  orphanNodes: RefChanges,
  ignoredNodes: RefChanges,
  networkDataUpdate: Option[NetworkDataUpdate],
  networkNodes: RefDiffs,
  routes: RefDiffs,
  nodes: IdDiffs,
  ways: IdDiffs,
  relations: IdDiffs,
  happy: Boolean,
  investigate: Boolean
) {

  def referencedElements: ReferencedElements = {

    val nodeIds: Set[Long] = (
      orphanNodes.oldRefs.map(_.id) ++
        ignoredNodes.oldRefs.map(_.id) ++
        orphanNodes.newRefs.map(_.id) ++
        ignoredNodes.newRefs.map(_.id) ++
        networkNodes.ids
      ).toSet

    val routeIds: Set[Long] = (
      orphanRoutes.oldRefs.map(_.id) ++
        ignoredRoutes.oldRefs.map(_.id) ++
        orphanRoutes.newRefs.map(_.id) ++
        ignoredRoutes.newRefs.map(_.id) ++
        routes.ids
      ).toSet

    ReferencedElements(nodeIds, routeIds)
  }

  override def toString: String = ToStringBuilder(this.getClass.getSimpleName).
    field("key", key).
    field("changeType", changeType).
    field("orphanRoutes", orphanRoutes).
    field("ignoredRoutes", ignoredRoutes).
    field("orphanNodes", orphanNodes).
    field("ignoredNodes", ignoredNodes).
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
    build
}
