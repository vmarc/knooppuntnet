package kpn.server.analyzer.engine.changes

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.custom.Subset
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges
import kpn.server.analyzer.engine.changes.route.OrphanRouteChange

class ChangeSetSummaryBuilder() {

  def build(
    replicationId: ReplicationId,
    changeSet: ChangeSet,
    changes: ChangeSetChanges
  ): ChangeSetSummary = {

    val networkChanges = toNetworkChanges(changes)
    val orphanRouteChanges = toOrphanRouteChanges(changes)
    val nodeChanges = toNodeChanges(changes)

    ChangeSetSummary(
      ChangeKey(
        replicationId.number,
        changeSet.timestamp,
        changeSet.id,
        0L
      ),
      changeSet.timestampFrom,
      changeSet.timestampUntil,
      networkChanges,
      orphanRouteChanges,
      nodeChanges
    )
  }

  private def toNetworkChanges(changes: ChangeSetChanges): NetworkChanges = {
    val creates = toChangeSetNetworks(changes.networkChanges, ChangeType.Create)
    val updates = toChangeSetNetworks(changes.networkChanges, ChangeType.Update)
    val deletes = toChangeSetNetworks(changes.networkChanges, ChangeType.Delete)
    NetworkChanges(creates, updates, deletes)
  }

  private def toChangeSetNetworks(networkChanges: Seq[NetworkChange], changeType: ChangeType): Seq[ChangeSetNetwork] = {

    val changeTypeNetworkChanges = networkChanges.filter(_.changeType == changeType)

    changeTypeNetworkChanges.map { networkChange =>

      val routeChanges = ChangeSetElementRefs(
        removed = networkChange.routes.removed.map(ref => toRef(ref, happy = false, investigate = true)),
        added = networkChange.routes.added.map(ref => toRef(ref, happy = true, investigate = false)),
        updated = networkChange.routes.updated.map(ref => toRef(ref, happy = false, investigate = false))
      )

      val nodeChanges = ChangeSetElementRefs(
        removed = networkChange.networkNodes.removed.map(ref => toRef(ref, happy = false, investigate = true)),
        added = networkChange.networkNodes.added.map(ref => toRef(ref, happy = true, investigate = false)),
        updated = networkChange.networkNodes.updated.map(ref => toRef(ref, happy = false, investigate = false))
      )

      ChangeSetNetwork(
        networkChange.country,
        networkChange.networkType,
        networkChange.networkId,
        networkChange.networkName,
        routeChanges,
        nodeChanges,
        networkChange.happy,
        networkChange.investigate
      )
    }
  }

  private def toRef(ref: Ref, happy: Boolean, investigate: Boolean): ChangeSetElementRef = {
    ChangeSetElementRef(
      ref.id,
      ref.name,
      happy,
      investigate
    )
  }

  private def toOrphanRouteChanges(orphanRouteChanges: ChangeSetChanges): Seq[ChangeSetSubsetElementRefs] = {

    val changes = orphanRouteChanges.routeChanges.filter(OrphanRouteChange.isOrphanRouteChange)
    val subsets = changes.flatMap(_.subsets).distinct.sorted

    subsets.flatMap { subset =>

      val removed = toRouteChangeRefs(changes, subset, ChangeType.Delete)
      val added = toRouteChangeRefs(changes, subset, ChangeType.Create)
      val updated = toRouteChangeRefs(changes, subset, ChangeType.Update)

      if (removed.nonEmpty || added.nonEmpty || updated.nonEmpty) {
        Some(
          ChangeSetSubsetElementRefs(
            subset,
            ChangeSetElementRefs(
              removed,
              added,
              updated
            )
          )
        )
      }
      else {
        None
      }
    }
  }

  private def toRouteChangeRefs(changes: Seq[RouteChange], subset: Subset, changeType: ChangeType): Seq[ChangeSetElementRef] = {
    val filteredChanges = changes.filter(_.changeType == changeType).filter(_.subsets.contains(subset))
    filteredChanges.map {
      change =>
        ChangeSetElementRef(
          change.id,
          change.name,
          change.happy,
          change.investigate
        )
    }
  }

  private def toNodeChanges(changeSetChanges: ChangeSetChanges): Seq[ChangeSetSubsetElementRefs] = {

    val subsets: Seq[Subset] = changeSetChanges.nodeChanges.flatMap(_.subsets).distinct.sorted

    subsets.flatMap {
      subset =>

        val removed = toNodeChangeRefs(changeSetChanges.nodeChanges, subset, ChangeType.Delete)
        val added = toNodeChangeRefs(changeSetChanges.nodeChanges, subset, ChangeType.Create)
        val updated = toNodeChangeRefs(changeSetChanges.nodeChanges, subset, ChangeType.Update)

        if (removed.nonEmpty || added.nonEmpty || updated.nonEmpty) {
          Some(
            ChangeSetSubsetElementRefs(
              subset,
              ChangeSetElementRefs(
                removed,
                added,
                updated
              )
            )
          )
        }
        else {
          None
        }
    }
  }

  private def toNodeChangeRefs(nodeChanges: Seq[NodeChange], subset: Subset, changeType: ChangeType): Seq[ChangeSetElementRef] = {
    val changes = nodeChanges.filter(_.changeType == changeType).filter(_.subsets.contains(subset))
    changes.map {
      change =>
        ChangeSetElementRef(
          change.id,
          change.name,
          change.happy,
          change.investigate
        )
    }
  }
}
