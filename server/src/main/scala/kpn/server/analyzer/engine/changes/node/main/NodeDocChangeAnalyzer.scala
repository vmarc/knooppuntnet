package kpn.server.analyzer.engine.changes.node.main

import kpn.api.common.ChangeType
import kpn.api.common.Fact
import kpn.api.common.LatLonImpl
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.Subset
import kpn.core.doc.NodeDoc
import kpn.core.history.NodeTagDiffAnalyzer
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.node.main.NodeChangeStateAnalyzer.analyzed
import kpn.server.analyzer.engine.tile.NodeTileChangeAnalyzer

class NodeDocChangeAnalyzer(
  tileChangeAnalyzer: NodeTileChangeAnalyzer,
  context: ChangeSetContext,
  before: NodeDoc,
  after: NodeDoc,
  facts: Seq[Fact],
  changeType: ChangeType
) {

  def analyze(): Option[NodeChange] = {

    val roleConnectionChanges = before.networkRelationReferences.map(_.id).intersect(after.networkRelationReferences.map(_.id)).flatMap { networkId =>
      val networkReferenceBefore = before.networkRelationReferences.find(_.id == networkId)
      val networkReferenceAfter = after.networkRelationReferences.find(_.id == networkId)
      val connectionBefore = networkReferenceBefore.flatMap(_.role).contains("connection")
      val connectionAfter = networkReferenceAfter.flatMap(_.role).contains("connection")
      if (connectionBefore != connectionAfter) {
        networkReferenceAfter.map(networkRef => RefBooleanChange(networkRef.toRef, connectionAfter))
      }
      else {
        None
      }
    }

    val beforeNetworkIds = before.networkRelationReferences.map(_.id).toSet
    val afterNetworkIds = after.networkRelationReferences.map(_.id).toSet
    val addedNetworkIds = (afterNetworkIds -- beforeNetworkIds).toSeq.sorted
    val removedNetworkIds = (beforeNetworkIds -- afterNetworkIds).toSeq.sorted

    val addedToNetwork = after.networkRelationReferences.filter(r => addedNetworkIds.contains(r.id)).map(_.toRef)
    val removedFromNetwork = before.networkRelationReferences.filter(r => removedNetworkIds.contains(r.id)).map(_.toRef)

    val beforeRouteIds = before.routeReferences.map(_.id).toSet
    val afterRouteIds = after.routeReferences.map(_.id).toSet
    val addedRouteIds = (afterRouteIds -- beforeRouteIds).toSeq.sorted
    val removedRouteIds = (beforeRouteIds -- afterRouteIds).toSeq.sorted
    val addedToRoute = after.routeReferences.filter(r => addedRouteIds.contains(r.id)).map(_.toRef)
    val removedFromRoute = before.routeReferences.filter(r => removedRouteIds.contains(r.id)).map(_.toRef)

    if (before.isSameAs(after) && addedToNetwork.isEmpty && removedFromNetwork.isEmpty && addedToRoute.isEmpty && removedFromRoute.isEmpty && roleConnectionChanges.isEmpty) {
      None
    }
    else {
      val subsets = {
        val subsetsBefore = before.base.country.toSeq.flatMap(country => before.base.names.map(_.routeType).flatMap(routeType => Subset.of(country, routeType)))
        val subsetsAfter = after.base.country.toSeq.flatMap(country => after.base.names.map(_.routeType).flatMap(routeType => Subset.of(country, routeType)))
        (subsetsBefore ++ subsetsAfter).distinct
      }
      val tagDiffs = analyzeTagDiffs
      val nodeMoved = analyzeNodeMoved
      val key = context.buildChangeKey(after._id)

      val allLocations = (before.base.locations ++ after.base.locations).distinct.sorted
      val impactedTiles = tileChangeAnalyzer.impactedTiles(before, after)

      val nodeName = changeType match {
        case ChangeType.Delete => before.base.name
        case _ => after.base.name
      }

      Some(
        analyzed(
          NodeChange(
            _id = key.toId,
            key = key,
            changeType = changeType,
            subsets = subsets,
            locations = allLocations,
            name = nodeName,
            before = Some(before.toMeta),
            after = Some(after.toMeta),
            connectionChanges = Seq.empty,
            roleConnectionChanges = roleConnectionChanges,
            definedInNetworkChanges = Seq.empty,
            tagDiffs = tagDiffs,
            nodeMoved = nodeMoved,
            addedToRoute = addedToRoute,
            removedFromRoute = removedFromRoute,
            addedToNetwork = addedToNetwork,
            removedFromNetwork = removedFromNetwork,
            factDiffs = factDiffs(),
            facts = facts,
            initialTags = None,
            initialLatLon = None,
          )
        )
      )
    }
  }

  private def analyzeTagDiffs: Option[TagDiffs] = {
    new NodeTagDiffAnalyzer(before, after).diffs
  }

  private def analyzeNodeMoved: Option[NodeMoved] = {
    Option.when(before.base.latitude != after.base.latitude || before.base.longitude != after.base.longitude) {
      val latLonBefore = LatLonImpl(before.base.latitude, before.base.longitude)
      val latLonAfter = LatLonImpl(after.base.latitude, after.base.longitude)
      val distance = Haversine.meters(Seq(before.base, after.base))
      NodeMoved(latLonBefore, latLonAfter, distance)
    }
  }

  private def factDiffs(): Option[FactDiffs] = {

    val beforeFacts = before.facts.toSet
    val afterFacts = after.facts.toSet

    val resolvedFacts = (beforeFacts -- afterFacts).toSeq
    val introducedFacts = (afterFacts -- beforeFacts).toSeq
    val remainingFacts = (afterFacts intersect beforeFacts).toSeq

    if (resolvedFacts.nonEmpty || introducedFacts.nonEmpty) {
      Some(
        FactDiffs(
          resolvedFacts,
          introducedFacts,
          remainingFacts
        )
      )
    }
    else {
      None
    }
  }
}
