package kpn.server.analyzer.engine.changes

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.LocationChangeSetSummary
import kpn.api.common.LocationChangesTree
import kpn.api.common.LocationChangesTreeNode
import kpn.api.common.ReplicationId
import kpn.api.common.changes.ChangeSet
import kpn.api.common.changes.details.ChangeKey
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.location.Location
import kpn.api.custom.NetworkType
import kpn.server.analyzer.engine.changes.data.ChangeSetChanges

class LocationChangeSetSummaryBuilder() {

  def build(
    replicationId: ReplicationId,
    changeSet: ChangeSet,
    changes: ChangeSetChanges
  ): LocationChangeSetSummary = {

    val trees: Seq[LocationChangesTree] = NetworkType.all.flatMap { networkType =>
      val nodeChanges = changes.nodeChanges.filter(_.subsets.map(_.networkType).contains(networkType))
      val routeChanges = changes.routeChanges.filter(_.subsets.map(_.networkType).contains(networkType))
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

    val happy = trees.exists(_.happy)
    val investigate = trees.exists(_.investigate)

    val key = ChangeKey(
      replicationId.number,
      changeSet.timestamp,
      changeSet.id,
      0L
    )

    LocationChangeSetSummary(
      key.toShortId,
      key,
      changeSet.timestampFrom,
      changeSet.timestampUntil,
      trees,
      happy,
      investigate,
      happy || investigate
    )
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
