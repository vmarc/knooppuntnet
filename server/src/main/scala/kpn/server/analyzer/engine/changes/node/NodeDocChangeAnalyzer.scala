package kpn.server.analyzer.engine.changes.node

import kpn.api.common.LatLonImpl
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RefBooleanChange
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.node.NodeMoved
import kpn.api.custom.ChangeType
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.core.doc.NodeDoc
import kpn.core.history.NodeTagDiffAnalyzer
import kpn.core.util.Haversine
import kpn.server.analyzer.engine.changes.ChangeSetContext
import kpn.server.analyzer.engine.changes.node.NodeChangeStateAnalyzer.analyzed

class NodeDocChangeAnalyzer(
  context: ChangeSetContext,
  before: NodeDoc,
  after: NodeDoc,
  facts: Seq[Fact],
  changeType: ChangeType
) {

  def analyze(): Option[NodeChange] = {

    val nodeId = before._id

    val roleConnectionChanges = context.changes.networkChanges.flatMap { networkChange =>
      if (networkChange.nodes.updated.contains(nodeId)) {
        context.elementChanges.relationGet(networkChange.networkId) match {
          case Some(rawRelationChange) =>
            val connectionBefore = rawRelationChange.before.nodeMembers.filter(_.ref == nodeId).exists(_.role.contains("connection"))
            val connectionAfter = rawRelationChange.after.nodeMembers.filter(_.ref == nodeId).exists(_.role.contains("connection"))
            if (connectionBefore != connectionAfter) {
              Some(
                RefBooleanChange(networkChange.toRef, connectionAfter)
              )
            }
            else {
              None
            }

          case _ => None
        }
      }
      else {
        None
      }
    }

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

    if (before.isSameAs(after) && addedToNetwork.isEmpty && removedFromNetwork.isEmpty && addedToRoute.isEmpty && removedFromRoute.isEmpty && roleConnectionChanges.isEmpty) {
      None
    }
    else {
      val subsets = {
        val subsetsBefore = before.country.toSeq.flatMap(country => before.names.map(_.networkType).flatMap(networkType => Subset.of(country, networkType)))
        val subsetsAfter = after.country.toSeq.flatMap(country => after.names.map(_.networkType).flatMap(networkType => Subset.of(country, networkType)))
        (subsetsBefore ++ subsetsAfter).distinct
      }
      val tagDiffs = analyzeTagDiffs
      val nodeMoved = analyzeNodeMoved
      val key = context.buildChangeKey(after._id)

      val allLocations = (before.locations ++ after.locations).distinct.sorted
      val allTiles = (before.tiles ++ after.tiles).distinct.sorted

      val nodeName = changeType match {
        case ChangeType.Delete => before.name
        case _ => after.name
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
