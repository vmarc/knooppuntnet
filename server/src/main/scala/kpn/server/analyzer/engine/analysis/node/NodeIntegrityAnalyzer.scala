package kpn.server.analyzer.engine.analysis.node

import kpn.core.analysis.NetworkMemberRoute
import kpn.core.analysis.NetworkNode
import kpn.server.analyzer.engine.analysis.network.NetworkAnalysis
import kpn.shared.NetworkType
import kpn.shared.NodeIntegrityCheck

class NodeIntegrityAnalyzer(networkType: NetworkType, networkAnalysis: NetworkAnalysis, networkNode: NetworkNode) {

  def analysis: Option[NodeIntegrityCheck] = {
    if (referencedInNetworkRelation && hasIntegrityCheck) {
      Some(NodeIntegrityCheck(networkNode.name, networkNode.node.id, routesWithNodeReference.size, expectedRouteRelationCount, failed))
    }
    else {
      None
    }
  }

  private def hasIntegrityCheck: Boolean = {
    networkType.scopedNetworkTypes.exists(n => networkNode.node.tags.has(n.expectedRouteRelationsTag))
  }

  private def expectedRouteRelationCount: Int = {
    // picks the first scoped networkType when there is more than one --> this is not entirely correct
    networkType.scopedNetworkTypes.flatMap(n => networkNode.node.tags(n.expectedRouteRelationsTag)).headOption match {
      case None => 0
      case Some(value) =>
        if (!value.forall(_.isDigit)) {
          0
        }
        else {
          value.toInt
        }
    }
  }

  private def referencedInNetworkRelation: Boolean = {
    networkAnalysis.networkNodesInRelation.map(_.id).contains(networkNode.id)
  }

  private def routesWithNodeReference: Seq[NetworkMemberRoute] = {
    networkAnalysis.routes.filterNot(hasSpecialState).filter(hasNodeReference)
  }

  private def failed: Boolean = {
    routesWithNodeReference.size != expectedRouteRelationCount
  }

  private def hasSpecialState(memberRoute: NetworkMemberRoute): Boolean = {
    val tags = memberRoute.routeAnalysis.route.tags
    tags.has("state", "connection") || tags.has("state", "alternate")
  }

  private def hasNodeReference(memberRoute: NetworkMemberRoute): Boolean = {
    memberRoute.routeAnalysis.routeNodes.nodesInWays.map(_.id).contains(networkNode.id)
  }
}
