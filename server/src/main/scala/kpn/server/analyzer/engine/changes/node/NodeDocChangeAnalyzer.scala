package kpn.server.analyzer.engine.changes.node

import kpn.api.common.LatLonImpl
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.ChangeType
import kpn.api.custom.Subset
import kpn.core.doc.NodeDoc
import kpn.core.history.NodeTagDiffAnalyzer
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.node.NodeChangeStateAnalyzer.analyzed

class NodeDocChangeAnalyzer(context: ChangeSetContext, before: NodeDoc, after: NodeDoc) {

  def analyze(): Option[NodeChange] = {

    val addedToNetwork = context.changes.networkChanges.filter { networkChange =>
      networkChange.nodes.added.contains(before._id)
    }.map(_.toRef)

    val removedFromNetwork = context.changes.networkChanges.filter { networkChange =>
      networkChange.nodes.removed.contains(before._id)
    }.map(_.toRef)

    val addedToRoute = context.changes.routeChanges.filter { routeChange =>
      val hasNodeBefore = routeChange.before.toSeq.flatMap(_.nodes).exists(_.id == before._id)
      val hasNodeAfter = routeChange.after.toSeq.flatMap(_.nodes).exists(_.id == before._id)
      !hasNodeBefore && hasNodeAfter
    }.map(_.toRef)

    val removedFromRoute = context.changes.routeChanges.filter { routeChange =>
      val hasNodeBefore = routeChange.before.toSeq.flatMap(_.nodes).exists(_.id == before._id)
      val hasNodeAfter = routeChange.after.toSeq.flatMap(_.nodes).exists(_.id == before._id)
      hasNodeBefore && !hasNodeAfter
    }.map(_.toRef)

    if (before.isSameAs(after) && addedToNetwork.isEmpty && removedFromNetwork.isEmpty && addedToRoute.isEmpty && removedFromRoute.isEmpty) {
      None
    }
    else {
      val subsetsBefore = before.country.toSeq.flatMap(country => before.names.map(_.networkType).flatMap(networkType => Subset.of(country, networkType)))
      val subsetsAfter = after.country.toSeq.flatMap(country => after.names.map(_.networkType).flatMap(networkType => Subset.of(country, networkType)))
      val subsets = (subsetsBefore ++ subsetsAfter).distinct
      val tagDiffs = analyzeTagDiffs
      val nodeMoved = analyzeNodeMoved
      val key = context.buildChangeKey(after._id)

      val allLocations = (before.locations ++ after.locations).distinct.sorted
      val allTiles = (before.tiles ++ after.tiles).distinct.sorted

      Some(
        analyzed(
          NodeChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Update,
            subsets = subsets,
            locations = allLocations,
            name = after.name,
            before = Some(before.toMeta),
            after = Some(after.toMeta),
            connectionChanges = Seq.empty,
            roleConnectionChanges = Seq.empty,
            definedInNetworkChanges = Seq.empty,
            tagDiffs = tagDiffs,
            nodeMoved = nodeMoved,
            addedToRoute = addedToRoute,
            removedFromRoute = removedFromRoute,
            addedToNetwork = addedToNetwork,
            removedFromNetwork = removedFromNetwork,
            factDiffs = FactDiffs(), // TODO MONGO should do better !!!
            facts = Seq.empty,
            initialTags = None,
            initialLatLon = None,
            allTiles
          )
        )
      )
    }
  }

  private def analyzeTagDiffs: Option[TagDiffs] = {
    new NodeTagDiffAnalyzer(before, after).diffs
  }

  private def analyzeNodeMoved: Option[NodeMoved] = {
    if (before.latitude != after.latitude || before.longitude != after.longitude) {
      val latLonBefore = LatLonImpl(before.latitude, before.longitude)
      val latLonAfter = LatLonImpl(after.latitude, after.longitude)
      val distance = Haversine.meters(Seq(before, after))
      Some(NodeMoved(latLonBefore, latLonAfter, distance))
    }
    else {
      None
    }
  }
}
