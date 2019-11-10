package kpn.core.history

import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.NetworkNodeData
import kpn.core.analysis.Network
import kpn.server.analyzer.engine.changes.diff.NetworkDiff
import kpn.server.analyzer.engine.changes.diff.NetworkNodeDiffs
import kpn.server.analyzer.engine.changes.diff.RouteDiffs

class NetworkCreateAnalyzer(network: Network) {

  private val addedNodes: Seq[Long] = network.extraMemberNodeIds
  private val addedWays: Seq[Long] = network.extraMemberWayIds
  private val addedRelations: Seq[Long] = network.extraMemberRelationIds

  private val addedNetworkNodes: Seq[NetworkNodeData] = {
    network.nodes.map { networkNodeInfo =>
      NetworkNodeData(
        networkNodeInfo.networkNode.node.raw,
        networkNodeInfo.networkNode.name,
        networkNodeInfo.networkNode.country
      )
    }.sortBy(_.name)
  }

  private val addedRoutes = network.routes.map(_.routeAnalysis).sortBy(_.name)

  private val happy: Boolean = {
    addedNetworkNodes.nonEmpty ||
      addedRoutes.nonEmpty
  }

  private val investigate: Boolean = {
    addedNodes.nonEmpty || // no need for this, will already be reported in network facts?
      addedWays.nonEmpty ||
      addedRelations.nonEmpty
  }

  val diff = NetworkDiff(
    network.country,
    network.networkType,
    network.id,
    network.name,
    None,
    NetworkNodeDiffs(Seq(), addedNetworkNodes, Seq()),
    RouteDiffs(Seq(), addedRoutes, Seq()),
    IdDiffs(Seq(), addedNodes, Seq()),
    IdDiffs(Seq(), addedWays, Seq()),
    IdDiffs(Seq(), addedRelations, Seq()),
    happy,
    investigate
  )
}
