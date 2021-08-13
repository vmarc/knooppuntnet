package kpn.server.analyzer.engine.changes.node

import kpn.api.common.LatLonImpl
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.Subset
import kpn.core.history.NodeTagDiffAnalyzer
import kpn.core.mongo.doc.NodeDoc
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.node.NodeChangeStateAnalyzer.analyzed

class NodeDocChangeAnalyzer(context: ChangeSetContext, before: NodeDoc, after: NodeDoc) {

  def analyze(): Option[NodeChange] = {

    if (before == after) {
      None
    }
    else {


      val subsetsBefore = before.country.toSeq.flatMap(country => before.names.map(_.networkType).flatMap(networkType => Subset.of(country, networkType)))
      val subsetsAfter = after.country.toSeq.flatMap(country => after.names.map(_.networkType).flatMap(networkType => Subset.of(country, networkType)))
      val subsets = (subsetsBefore ++ subsetsAfter).distinct
      val tagDiffs = analyzeTagDiffs
      val nodeMoved = analyzeNodeMoved
      val key = context.buildChangeKey(after._id)
      Some(
        analyzed(
          NodeChange(
            _id = key.toId,
            key = key,
            changeType = ChangeType.Update,
            subsets = subsets,
            locations = (before.locations ++ after.locations).distinct.sorted,
            name = after.name,
            before = Some(before.toMeta),
            after = Some(after.toMeta),
            connectionChanges = Seq.empty,
            roleConnectionChanges = Seq.empty,
            definedInNetworkChanges = Seq.empty,
            tagDiffs = tagDiffs,
            nodeMoved = nodeMoved,
            addedToRoute = Seq.empty, // TODO MONGO should do better !!!
            removedFromRoute = Seq.empty, // TODO MONGO should do better !!!
            addedToNetwork = Seq.empty,
            removedFromNetwork = Seq.empty,
            factDiffs = FactDiffs(), // TODO MONGO should do better !!!
            facts = Seq.empty,
            (before.tiles ++ after.tiles).distinct.sorted
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
