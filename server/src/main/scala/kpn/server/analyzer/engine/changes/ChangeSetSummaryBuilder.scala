package kpn.server.analyzer.engine.changes

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetNetwork
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.LocationChangesTree
import kpn.api.common.LocationChangesTreeNode
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

    val networkChanges = toNetworkChanges(context)
    val routeChanges = toRouteChanges(context)
    val nodeChanges = toNodeChanges(context)

    val trees = buildTrees(context)

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
      nodeChanges,
      trees
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
        removed = networkChange.routeDiffs.removed.map(ref => toRef(ref, happy = false, investigate = true)),
        added = networkChange.routeDiffs.added.map(ref => toRef(ref, happy = true, investigate = false)),
        updated = networkChange.routeDiffs.updated.map(ref => toRef(ref, happy = false, investigate = false))
      )

      val nodeChanges = ChangeSetElementRefs(
        removed = networkChange.nodeDiffs.removed.map(ref => toRef(ref, happy = false, investigate = true)),
        added = networkChange.nodeDiffs.added.map(ref => toRef(ref, happy = true, investigate = false)),
        updated = networkChange.nodeDiffs.updated.map(ref => toRef(ref, happy = false, investigate = false))
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

  private def buildTrees(context: ChangeSetContext): Seq[LocationChangesTree] = {
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

      val leafNodes = locations.map { location =>
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

        LocationChangesTreeNode(
          locationName = location.names.last,
          routeChanges = leafNodeRouteChanges,
          nodeChanges = leafNodeNodeChanges,
          children = Seq.empty,
          happy = happy,
          investigate = investigate
        )
      }

      val locationRoots = locations.map(_.names.head).distinct.sorted

      locationRoots.map { root =>
        val locs = locations.filter(_.names.head == root)
        val children = buildTree(1, locs, leafNodes)
        val happy = children.exists(_.happy)
        val investigate = children.exists(_.investigate)
        LocationChangesTree(
          networkType = networkType,
          locationName = root,
          happy = happy,
          investigate = investigate,
          children = children
        )
      }
    }
  }

  private def buildTree(level: Int, locations: Seq[Location], leafNodes: Seq[LocationChangesTreeNode]): Seq[LocationChangesTreeNode] = {

    val locationNames = locations.map(_.names(level)).distinct.sorted

    locationNames.map { locationName =>
      leafNodes.find(_.locationName == locationName) match {
        case Some(leafNode) => leafNode
        case None =>
          val locs = locations.filter(_.names(level) == locationName)
          val children = buildTree(level + 1, locs, leafNodes)
          val happy = children.exists(_.happy)
          val investigate = children.exists(_.investigate)
          LocationChangesTreeNode(
            locationName = locationName,
            routeChanges = ChangeSetElementRefs(),
            nodeChanges = ChangeSetElementRefs(),
            children = children,
            happy = happy,
            investigate = investigate
          )
      }
    }
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
}

