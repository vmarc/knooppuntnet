package kpn.server.analyzer.engine.changes

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.custom.Subset

class ChangeSetSummaryBuilder() {

  def build(context: ChangeSetContext): ChangeSetSummary = {

    val networkChanges = toNetworkChanges(context)
    val routeChanges = toRouteChanges(context)
    val nodeChanges = toNodeChanges(context)

    ChangeSetSummary(
      ChangeKey(
        context.replicationId.number,
        context.changeSet.timestamp,
        context.changeSet.id,
        0L
      ),
      context.changeSet.timestampFrom,
      context.changeSet.timestampUntil,
      networkChanges,
      routeChanges,
      nodeChanges
    )
  }

  private def toNetworkChanges(context: ChangeSetContext): NetworkChanges = {
    val creates = toChangeSetNetworks(context.changes.networkInfoChanges, ChangeType.Create)
    val updates = toChangeSetNetworks(context.changes.networkInfoChanges, ChangeType.Update)
    val deletes = toChangeSetNetworks(context.changes.networkInfoChanges, ChangeType.Delete)
    NetworkChanges(creates, updates, deletes)
  }

  private def toChangeSetNetworks(networkChanges: Seq[NetworkInfoChange], changeType: ChangeType): Seq[ChangeSetNetwork] = {

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

  private def toRouteChanges(context: ChangeSetContext): Seq[ChangeSetSubsetElementRefs] = {

    val changes = context.changes.routeChanges
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

  private def toNodeChanges(context: ChangeSetContext): Seq[ChangeSetSubsetElementRefs] = {

    val subsets: Seq[Subset] = context.changes.nodeChanges.flatMap(_.subsets).distinct.sorted

    subsets.flatMap {
      subset =>

        val removed = toNodeChangeRefs(context.changes.nodeChanges, subset, ChangeType.Delete)
        val added = toNodeChangeRefs(context.changes.nodeChanges, subset, ChangeType.Create)
        val updated = toNodeChangeRefs(context.changes.nodeChanges, subset, ChangeType.Update)

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
