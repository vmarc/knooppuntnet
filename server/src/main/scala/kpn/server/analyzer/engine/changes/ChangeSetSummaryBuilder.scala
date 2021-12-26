package kpn.server.analyzer.engine.changes

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.LocationChanges
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.NetworkInfoChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.common.location.Location
import kpn.api.custom.ChangeType
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset

class ChangeSetSummaryBuilder() {

  def build(context: ChangeSetContext): ChangeSetSummary = {

    val networkChanges = buildNetworkChanges(context)
    val orphanRouteChanges = buildOrphanRouteChanges(context)
    val orphanNodeChanges = buildOrphanNodeChanges(context)
    val locationChanges = buildLocationChanges(context)

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
      orphanRouteChanges,
      orphanNodeChanges,
      locationChanges
    )
  }

  private def buildNetworkChanges(context: ChangeSetContext): NetworkChanges = {
    val creates = toChangeSetNetworks(context, context.changes.networkInfoChanges, ChangeType.Create)
    val updates = toChangeSetNetworks(context, context.changes.networkInfoChanges, ChangeType.Update)
    val deletes = toChangeSetNetworks(context, context.changes.networkInfoChanges, ChangeType.Delete)
    NetworkChanges(creates, updates, deletes)
  }

  private def toChangeSetNetworks(context: ChangeSetContext, networkChanges: Seq[NetworkInfoChange], changeType: ChangeType): Seq[ChangeSetNetwork] = {

    val changeTypeNetworkChanges = networkChanges.filter(_.changeType == changeType)

    changeTypeNetworkChanges.map { networkChange =>

      val routeChanges = ChangeSetElementRefs(
        removed = networkChange.routeDiffs.removed.map { ref =>
          toRef(ref, happy = false, investigate = true)
        },
        added = networkChange.routeDiffs.added.map { ref =>
          toRef(ref, happy = true, investigate = isRouteInvestigate(context, ref.id))
        },
        updated = networkChange.routeDiffs.updated.map { ref =>
          toRef(ref, happy = false, investigate = isRouteInvestigate(context, ref.id))
        }
      )

      val nodeChanges = ChangeSetElementRefs(
        removed = networkChange.nodeDiffs.removed.map { ref =>
          toRef(ref, happy = false, investigate = true)
        },
        added = networkChange.nodeDiffs.added.map { ref =>
          toRef(ref, happy = true, investigate = isNodeInvestigate(context, ref.id))
        },
        updated = networkChange.nodeDiffs.updated.map { ref =>
          toRef(ref, happy = false, investigate = isNodeInvestigate(context, ref.id))
        }
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

  private def buildOrphanRouteChanges(context: ChangeSetContext): Seq[ChangeSetSubsetElementRefs] = {

    val referencedRouteIds = context.changes.networkInfoChanges.flatMap(_.routeDiffs.ids)
    val orphanRouteChanges = context.changes.routeChanges.filter(routeChange => !referencedRouteIds.contains(routeChange.id))
    val subsets = orphanRouteChanges.flatMap(_.subsets).distinct.sorted

    subsets.flatMap { subset =>

      val removed = toRouteChangeRefs(orphanRouteChanges, subset, ChangeType.Delete)
      val added = toRouteChangeRefs(orphanRouteChanges, subset, ChangeType.Create)
      val updated = toRouteChangeRefs(orphanRouteChanges, subset, ChangeType.Update)

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

  private def buildOrphanNodeChanges(context: ChangeSetContext): Seq[ChangeSetSubsetElementRefs] = {

    val orphanNodeChanges = collectOrphanNodeChanges(context)
    val subsets = orphanNodeChanges.flatMap(_.subsets).distinct.sorted

    subsets.flatMap {
      subset =>

        val removed = toNodeChangeRefs(orphanNodeChanges, subset, ChangeType.Delete)
        val added = toNodeChangeRefs(orphanNodeChanges, subset, ChangeType.Create)
        val updated = toNodeChangeRefs(orphanNodeChanges, subset, ChangeType.Update)

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

  private def buildLocationChanges(context: ChangeSetContext): Seq[LocationChanges] = {
    NetworkType.all.flatMap { networkType =>
      val nodeChanges = context.changes.nodeChanges.filter(_.subsets.map(_.networkType).contains(networkType))
      val routeChanges = context.changes.routeChanges.filter(_.subsets.map(_.networkType).contains(networkType))
      val locations = {
        val nodeLocations = nodeChanges.flatMap { nodeChange =>
          if (nodeChange.locations.nonEmpty) {
            Some(
              Location(nodeChange.locations)
            )
          }
          else {
            None
          }
        }
        val routeLocations = routeChanges.flatMap(_.locationAnalysis.candidates.map(_.location))
        (nodeLocations ++ routeLocations).distinct
      }

      locations.map { location =>
        val leafNodeNodeChanges = {
          val locationNodeChanges = nodeChanges.filter(_.locations == location.names)
          val removed = locationNodeChanges.filter(_.changeType == ChangeType.Delete).map(toRef)
          val added = locationNodeChanges.filter(_.changeType == ChangeType.Create).map(toRef)
          val updated = locationNodeChanges.filter(_.changeType == ChangeType.Update).map(toRef)
          ChangeSetElementRefs(
            removed,
            added,
            updated
          )
        }

        val leafNodeRouteChanges = {
          val locationRouteChanges = routeChanges.filter(_.locationAnalysis.candidates.map(_.location).contains(location))
          val removed = locationRouteChanges.filter(_.changeType == ChangeType.Delete).map(toRef)
          val added = locationRouteChanges.filter(_.changeType == ChangeType.Create).map(toRef)
          val updated = locationRouteChanges.filter(_.changeType == ChangeType.Update).map(toRef)
          ChangeSetElementRefs(
            removed,
            added,
            updated
          )
        }

        val happy = leafNodeNodeChanges.removed.exists(_.happy) || leafNodeNodeChanges.added.exists(_.happy) || leafNodeNodeChanges.updated.exists(_.happy)
        val investigate = leafNodeNodeChanges.removed.exists(_.investigate) || leafNodeNodeChanges.added.exists(_.investigate) || leafNodeNodeChanges.updated.exists(_.investigate)

        LocationChanges(
          networkType,
          location.names,
          leafNodeRouteChanges,
          leafNodeNodeChanges,
          happy,
          investigate
        )
      }
    }
  }

  private def collectOrphanNodeChanges(context: ChangeSetContext): Seq[NodeChange] = {
    val networkChangeReferencedNodeIds = context.changes.networkInfoChanges.flatMap(_.nodeDiffs.ids)
    val routeChangeReferencedNodeIds = context.changes.routeChanges.flatMap(_.diffs.nodeDiffs.map(_.referencedNodeIds))
    val referencedNodeIds = networkChangeReferencedNodeIds ++ routeChangeReferencedNodeIds
    context.changes.nodeChanges.filter(nodeChange => !referencedNodeIds.contains(nodeChange.id))
  }

  private def toRef(nodeChange: NodeChange): ChangeSetElementRef = {
    ChangeSetElementRef(
      id = nodeChange.key.elementId,
      name = nodeChange.name,
      happy = nodeChange.locationHappy,
      investigate = nodeChange.locationInvestigate
    )
  }

  private def toRef(routeChange: RouteChange): ChangeSetElementRef = {
    ChangeSetElementRef(
      id = routeChange.key.elementId,
      name = routeChange.name,
      happy = routeChange.locationHappy,
      investigate = routeChange.locationInvestigate
    )
  }

  private def isRouteInvestigate(context: ChangeSetContext, routeId: Long): Boolean = {
    context.changes.routeChanges.filter(routeChange => routeChange.id == routeId).exists(_.investigate)
  }

  private def isNodeInvestigate(context: ChangeSetContext, nodeId: Long): Boolean = {
    context.changes.nodeChanges.filter(nodeChange => nodeChange.id == nodeId).exists(_.investigate)
  }
}
