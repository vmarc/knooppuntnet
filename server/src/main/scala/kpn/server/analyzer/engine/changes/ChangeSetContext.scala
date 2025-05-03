package kpn.server.analyzer.engine.changes

import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.details.ChangeKey
import kpn.api.custom.Timestamp
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.context.ElementIds

case class ChangeSetContext(
  replicationId: ReplicationId,
  changeSet: ChangeSet,
  elementIds: ElementIds,
  changes: ChangeSetChanges = ChangeSetChanges(),
  elementChanges: ElementChangeMap = new ElementChangeMap(),
  baseNetworkCreateIds: Seq[Long] = Seq.empty,
  baseNetworkUpdateIds: Seq[Long] = Seq.empty,
  baseNetworkDeleteIds: Seq[Long] = Seq.empty,
  baseRouteCreatedIds: Seq[Long] = Seq.empty,
  baseRouteUpdatedIds: Seq[Long] = Seq.empty,
  baseRouteDeletedIds: Seq[Long] = Seq.empty,
  impactedTiles: Seq[String] = Seq.empty,

  // the elements to be processed in phase II:
  impactedNodeIds: Seq[Long] = Seq.empty,
  impactedRouteIds: Seq[Long] = Seq.empty,
  impactedNetworkIds: Seq[Long] = Seq.empty,
) {

  def timestampBefore: Timestamp = changeSet.timestampBefore

  def timestampAfter: Timestamp = changeSet.timestampAfter

  def buildChangeKey(elementId: Long): ChangeKey = {
    ChangeKey(
      replicationId.number,
      changeSet.timestamp,
      changeSet.id,
      elementId
    )
  }

  def withImpact(
    nodeIds: Seq[Long] = Seq.empty,
    routeIds: Seq[Long] = Seq.empty,
    networkIds: Seq[Long] = Seq.empty,
    tiles: Seq[String] = Seq.empty,
  ): ChangeSetContext = {
    copy(
      impactedNodeIds = (impactedNodeIds ++ nodeIds).sorted.distinct,
      impactedRouteIds = (impactedRouteIds ++ routeIds).sorted.distinct,
      impactedNetworkIds = (impactedNetworkIds ++ networkIds).sorted.distinct,
      impactedTiles = (impactedTiles ++ tiles).sorted.distinct
    )
  }
}
