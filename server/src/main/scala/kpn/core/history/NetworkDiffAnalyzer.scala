package kpn.core.history

import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.NetworkNodeData
import kpn.api.common.diff.NetworkNodeUpdate
import kpn.core.analysis.NetworkMemberRoute
import kpn.core.analysis.NetworkNodeInfo
import kpn.server.analyzer.engine.analysis.route.RouteAnalysis
import kpn.server.analyzer.engine.changes.diff.NetworkDiff
import kpn.server.analyzer.engine.changes.diff.NetworkNodeDiffs
import kpn.server.analyzer.engine.changes.diff.RouteDiffs
import kpn.server.analyzer.engine.changes.diff.RouteUpdate

class NetworkDiffAnalyzer(before: NetworkSnapshot, after: NetworkSnapshot) {

  private val networkNodeIdsBefore: Set[Long] = before.network.nodes.map(_.networkNode.id).toSet
  private val networkNodeIdsAfter: Set[Long] = after.network.nodes.map(_.networkNode.id).toSet
  private val networkNodeIdsCommon: Set[Long] = networkNodeIdsBefore intersect networkNodeIdsAfter

  private val routeIdsBefore: Set[Long] = before.network.routes.map(_.routeAnalysis.route.id).toSet
  private val routeIdsAfter: Set[Long] = after.network.routes.map(_.routeAnalysis.route.id).toSet
  private val routeIdsCommon: Set[Long] = routeIdsBefore intersect routeIdsAfter

  private val nodeIdsBefore: Set[Long] = before.network.extraMemberNodeIds.toSet
  private val nodeIdsAfter: Set[Long] = after.network.extraMemberNodeIds.toSet
  private val nodeIdsCommon: Set[Long] = nodeIdsBefore intersect nodeIdsAfter

  private val wayIdsBefore: Set[Long] = before.networkRelation.wayMembers.map(_.way.id).toSet
  private val wayIdsAfter: Set[Long] = after.networkRelation.wayMembers.map(_.way.id).toSet
  private val wayIdsCommon: Set[Long] = wayIdsBefore intersect wayIdsAfter

  private val relationIdsBefore: Set[Long] = before.networkRelation.relationMembers.map(_.relation.id).toSet -- routeIdsBefore
  private val relationIdsAfter: Set[Long] = after.networkRelation.relationMembers.map(_.relation.id).toSet -- routeIdsAfter
  private val relationIdsCommon: Set[Long] = relationIdsBefore intersect relationIdsAfter

  private val removedNodes: Seq[Long] = (nodeIdsBefore -- nodeIdsAfter).toSeq.sorted
  private val addedNodes: Seq[Long] = (nodeIdsAfter -- nodeIdsBefore).toSeq.sorted
  private val updatedNodes: Seq[Long] = nodeIdsCommon.filter(id => before.data.nodes.get(id) != after.data.nodes.get(id)).toSeq.sorted

  private val removedWays: Seq[Long] = (wayIdsBefore -- wayIdsAfter).toSeq.sorted
  private val addedWays: Seq[Long] = (wayIdsAfter -- wayIdsBefore).toSeq.sorted
  private val updatedWays: Seq[Long] = wayIdsCommon.filter(id => before.data.ways.get(id) != after.data.ways.get(id)).toSeq.sorted

  private val removedRelations: Seq[Long] = (relationIdsBefore -- relationIdsAfter).toSeq.sorted
  private val addedRelations: Seq[Long] = (relationIdsAfter -- relationIdsBefore).toSeq.sorted
  private val updatedRelations: Seq[Long] = relationIdsCommon.filter(id => before.data.relations.get(id) != after.data.relations.get(id)).toSeq.sorted

  private val networkDataUpdate: Option[NetworkDataUpdate] = {

    // TODO CHANGE add diffs at network level - this should include a diff object if just member sorting order was changed

    if (before.networkRelation != after.networkRelation) {
      val networkDataBefore = NetworkData(before.networkRelation.raw, before.network.name)
      val networkDataAfter = NetworkData(after.networkRelation.raw, after.network.name)
      Some(NetworkDataUpdate(networkDataBefore, networkDataAfter))
    }
    else {
      None
    }
  }

  private val removedNetworkNodes: Seq[NetworkNodeData] = {
    (networkNodeIdsBefore -- networkNodeIdsAfter).flatMap { nodeId =>
      nodeBefore(nodeId).map { nodeInfo =>
        NetworkNodeData(
          nodeInfo.networkNode.node.raw, // TODO CHANGE was: nodeInfo.networkNode.name,
          nodeInfo.networkNode.name
        ) // TODO CHANGE add facts (add refs?)
      }
    }.toSeq.sortBy(_.name)
  }

  private val addedNetworkNodes: Seq[NetworkNodeData] = {
    (networkNodeIdsAfter -- networkNodeIdsBefore).flatMap { nodeId =>
      nodeAfter(nodeId).map { nodeInfo =>
        // val diffs: Seq[Diff] = Seq(
        //   TODO CHANGE should use node related facts for this, so that we see resolved situation more easily later
        //   if (!nodeInfo.definedInRelation) Some(NodeNotInNetworkRelation()) else None,
        //   if (nodeInfo.referencedInRoutes.isEmpty) Some(NodeNotInRoute()) else None,
        //   if (nodeInfo.facts.integrityCheck.isDefined && nodeInfo.facts.integrityCheck.get.failed) Some(NodeIntegrityCheckFailed(nodeInfo.facts.integrityCheck.get)) else None
        // ).flatten
        NetworkNodeData(
          after.data.nodes(nodeId).raw,
          nodeInfo.networkNode.name
        ) // TODO CHANGE add facts (add refs?)
      }
    }.toSeq.sortBy(_.name)
  }

  private val updatedNetworkNodes: Seq[NetworkNodeUpdate] = {
    val updates: Seq[NetworkNodeUpdate] = networkNodeIdsCommon.toSeq.flatMap { nodeId =>
      val na = nodeAfter(nodeId)
      val nb = nodeBefore(nodeId)
      if (na.isDefined && nb.isDefined) {
        val diffs = new NetworkNodeDiffAnalyzer(before.network.networkType, nb.get, na.get).diffs
        if (diffs.nonEmpty) {
          Some(
            NetworkNodeUpdate(
              NetworkNodeData(
                before.data.nodes(nodeId).raw,
                nb.get.networkNode.name
              ),
              NetworkNodeData(
                after.data.nodes(nodeId).raw,
                na.get.networkNode.name
              ),
              diffs
              // TODO CHANGE add Option[NodeMoved] here
            )
          )
        }
        else {
          None
        }
      }
      else {
        None
      }
    }
    updates.sortBy(_.name)
  }

  private val removedRoutes: Seq[RouteAnalysis] = {
    (routeIdsBefore -- routeIdsAfter).flatMap { routeId =>
      routeBefore(routeId).map(_.routeAnalysis)
    }.toSeq.sortBy(_.name)
  }

  private val addedRoutes: Seq[RouteAnalysis] = {
    (routeIdsAfter -- routeIdsBefore).flatMap { routeId =>
      routeAfter(routeId).map(_.routeAnalysis)
    }.toSeq.sortBy(_.name)
  }

  private val updatedRoutes: Seq[RouteUpdate] = {
    val updates: Seq[RouteUpdate] = routeIdsCommon.toSeq.flatMap { routeId =>
      new NetworkRouteDiffAnalyzer(before, after, routeId).analysis
    }
    updates.sortBy(_.name)
  }

  private val happy: Boolean = {
    (networkDataUpdate.isDefined && networkDataUpdate.get.happy) ||
      addedNetworkNodes.nonEmpty ||
      updatedNetworkNodes.exists(_.happy) ||
      addedRoutes.nonEmpty ||
      updatedRoutes.exists(_.happy) ||
      removedNodes.nonEmpty || // no need for this, will already be reported in network facts?
      removedWays.nonEmpty || // no need for this, will already be reported in network facts?
      removedRelations.nonEmpty // no need for this, will already be reported in network facts?
  }

  def diff: NetworkDiff = NetworkDiff(
    after.network.country,
    after.network.networkType,
    after.id,
    after.name,
    networkDataUpdate,
    NetworkNodeDiffs(removedNetworkNodes, addedNetworkNodes, updatedNetworkNodes),
    RouteDiffs(removedRoutes, addedRoutes, updatedRoutes),
    IdDiffs(removedNodes, addedNodes, updatedNodes),
    IdDiffs(removedWays, addedWays, updatedWays),
    IdDiffs(removedRelations, addedRelations, updatedRelations),
    happy,
    investigate()
  )

  private def investigate(): Boolean = {
    (networkDataUpdate.isDefined && networkDataUpdate.get.investigate) ||
      removedNetworkNodes.nonEmpty ||
      removedRoutes.nonEmpty ||
      updatedRoutes.exists(_.investigate) ||
      addedNodes.nonEmpty || // no need for this, will already be reported in network facts?
      addedWays.nonEmpty ||
      addedRelations.nonEmpty
  }

  private def nodeBefore(id: Long): Option[NetworkNodeInfo] = before.network.nodes.find(_.networkNode.id == id)

  private def nodeAfter(id: Long): Option[NetworkNodeInfo] = after.network.nodes.find(_.networkNode.id == id)

  private def routeAfter(id: Long): Option[NetworkMemberRoute] = after.network.routes.find(_.routeAnalysis.route.id == id)

  private def routeBefore(id: Long): Option[NetworkMemberRoute] = before.network.routes.find(_.routeAnalysis.route.id == id)

}
