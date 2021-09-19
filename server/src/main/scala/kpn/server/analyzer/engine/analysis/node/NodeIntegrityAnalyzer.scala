package kpn.server.analyzer.engine.analysis.node

import kpn.api.common.NodeIntegrityCheck
import kpn.api.custom.ScopedNetworkType
import kpn.core.analysis.NetworkMemberRoute
import kpn.core.analysis.NetworkNode
import kpn.server.analyzer.engine.analysis.network.NetworkAnalysis
import kpn.server.repository.NodeRouteRepository

class NodeIntegrityAnalyzer(
  nodeRouteRepository: NodeRouteRepository,
  scopedNetworkType: ScopedNetworkType,
  networkAnalysis: NetworkAnalysis,
  networkNode: NetworkNode
) {

  def analysis: Option[NodeIntegrityCheck] = {
    if (referencedInNetworkRelation && hasIntegrityCheck) {
      val actual = determineRouteReferenceCount()
      val expected = determineExpectedRouteRelationCount()
      val failed = actual != expected
      Some(
        NodeIntegrityCheck(
          networkNode.name,
          networkNode.node.id,
          actual,
          expected,
          failed
        )
      )
    }
    else {
      None
    }
  }

  private def hasIntegrityCheck: Boolean = {
    networkNode.node.tags.has(scopedNetworkType.expectedRouteRelationsTag)
  }

  private def determineExpectedRouteRelationCount(): Long = {
    networkNode.node.tags(scopedNetworkType.expectedRouteRelationsTag) match {
      case None => 0
      case Some(value) =>
        if (!value.forall(_.isDigit)) {
          0L
        }
        else {
          value.toLong
        }
    }
  }

  private def referencedInNetworkRelation: Boolean = {
    networkAnalysis.networkNodesInRelation.map(_.id).contains(networkNode.id)
  }

  private def determineRouteReferenceCount(): Long = {
    val networkRouteRefIds = networkAnalysis.routes.filterNot(hasSpecialState).filter(hasNodeReference).map(_.id)
    val allRouteRefIds = nodeRouteRepository.nodeRouteReferences(scopedNetworkType, networkNode.id, stale = false).map(_.id)
    (networkRouteRefIds ++ allRouteRefIds).distinct.size
  }

  private def hasSpecialState(memberRoute: NetworkMemberRoute): Boolean = {
    val tags = memberRoute.routeAnalysis.route.tags
    tags.has("state", "connection") || tags.has("state", "alternate")
  }

  private def hasNodeReference(memberRoute: NetworkMemberRoute): Boolean = {
    memberRoute.routeAnalysis.routeNodeAnalysis.nodesInWays.map(_.id).contains(networkNode.id)
  }
}
