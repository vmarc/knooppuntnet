package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeIntegrityCheck
import kpn.api.custom.ScopedNetworkType
import kpn.core.analysis.NetworkMemberRoute
import kpn.core.analysis.NetworkNode
import kpn.core.analysis.TagInterpreter
import kpn.server.analyzer.engine.analysis.network.NetworkAnalysis

class NodeIntegrityAnalyzer(scopedNetworkType: ScopedNetworkType, networkAnalysis: NetworkAnalysis, networkNode: NetworkNode) {

  def analysis: Option[NodeIntegrityCheck] = {
    if (referencedInNetworkRelation) {
      TagInterpreter.expectedRouteRelationCount(scopedNetworkType, networkNode.node.tags) match {
        case None => None
        case Some(expectedRouteRelationCount) =>
          val failed = routesWithNodeReference.size != expectedRouteRelationCount
          Some(
            NodeIntegrityCheck(
              networkNode.name,
              networkNode.node.id,
              routesWithNodeReference.size,
              expectedRouteRelationCount,
              failed
            )
          )
      }
    }
    else {
      None
    }
  }

  private def referencedInNetworkRelation: Boolean = {
    networkAnalysis.networkNodesInRelation.map(_.id).contains(networkNode.id)
  }

  private def routesWithNodeReference: Seq[NetworkMemberRoute] = {
    networkAnalysis.routes.filterNot(hasSpecialState).filter(hasNodeReference)
  }

  private def hasSpecialState(memberRoute: NetworkMemberRoute): Boolean = {
    val tags = memberRoute.routeAnalysis.route.tags
    tags.has("state", "connection") || tags.has("state", "alternate")
  }

  private def hasNodeReference(memberRoute: NetworkMemberRoute): Boolean = {
    memberRoute.routeAnalysis.routeNodeAnalysis.nodesInWays.map(_.id).contains(networkNode.id)
  }
}
