package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeIntegrityCheck
import kpn.api.custom.ScopedRouteType
import kpn.core.analysis.NetworkMemberRoute
import kpn.core.analysis.NetworkNode
import kpn.core.analysis.TagInterpreter
import kpn.server.analyzer.engine.analysis.network.NetworkAnalysis

class NodeIntegrityAnalyzer(scopedRouteType: ScopedRouteType, networkAnalysis: NetworkAnalysis, networkNode: NetworkNode) {

  def analysis: Option[NodeIntegrityCheck] = {
    if (referencedInNetworkRelation) {
      TagInterpreter.expectedRouteRelationCount(scopedRouteType, networkNode.node).map { expectedRouteRelationCount =>
        val routeRelationCount = routesWithNodeReference.size
        val failed = routeRelationCount != expectedRouteRelationCount
        NodeIntegrityCheck(
          networkNode.name,
          networkNode.node.id,
          routesWithNodeReference.size,
          expectedRouteRelationCount,
          failed
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
    memberRoute.data.hasTag("state", "connection") || memberRoute.data.hasTag("state", "alternate")
  }

  private def hasNodeReference(memberRoute: NetworkMemberRoute): Boolean = {
    memberRoute.data.networkNodes.map(_.nodeId).contains(networkNode.id)
  }
}
