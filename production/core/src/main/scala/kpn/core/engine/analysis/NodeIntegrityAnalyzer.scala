package kpn.core.engine.analysis

import kpn.core.analysis.NetworkNode
import kpn.shared.NetworkType
import kpn.shared.NodeIntegrityCheck

class NodeIntegrityAnalyzer(networkAnalysis: NetworkAnalysis, networkType: NetworkType, networkNode: NetworkNode) {

  private val tagKey = "expected_" + networkType.name + "_route_relations"

  private val hasIntegrityCheck = networkNode.node.tags.has(tagKey)

  private val expectedRouteRelationCount = if (hasIntegrityCheck) {
    val value = networkNode.node.tags(tagKey).get
    if (!value.forall(_.isDigit)) {
      // TODO facts ++= add network fact if some integrityCheckNode found
      0
    }
    else {
      value.toInt
    }
  }
  else {
    0
  }

  private val referencedInNetworkRelation = networkAnalysis.networkNodesInRelation.map(_.id).contains(networkNode.id)

  private val routesWithNodeReference = {
    val routesWithoutSpecialState = networkAnalysis.routes.filterNot { memberRoute =>
      val tags = memberRoute.routeAnalysis.route.tags
      tags.has("state", "connection") || tags.has("state", "alternate")
    }
    routesWithoutSpecialState.filter(_.routeAnalysis.routeNodes.nodesInWays.map(_.id).contains(networkNode.id))
  }

  private val failed = {
    if (referencedInNetworkRelation && hasIntegrityCheck) {
      routesWithNodeReference.size != expectedRouteRelationCount
    }
    else {
      false
    }
  }

  val analysis: Option[NodeIntegrityCheck] = if (referencedInNetworkRelation && hasIntegrityCheck) {
    Some(NodeIntegrityCheck(networkNode.name, networkNode.node.id, routesWithNodeReference.size, expectedRouteRelationCount, failed))
  }
  else {
    None
  }
}
