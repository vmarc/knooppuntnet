package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.ScopedNetworkType
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

object NodeIntegrityAnalyzer extends NodeAspectAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    new NodeIntegrityAnalyzer(analysis).analyze
  }
}

class NodeIntegrityAnalyzer(analysis: NodeAnalysis) {

  def analyze: NodeAnalysis = {

    val nodeIntegrityDetails = ScopedNetworkType.all.flatMap { scopedNetworkType =>
      analysis.node.tags(scopedNetworkType.expectedRouteRelationsTag) match {
        case None => None
        case Some(expectedRouteRelationsValue) =>
          if (expectedRouteRelationsValue.forall(Character.isDigit)) {
            val expectedRouteCount = expectedRouteRelationsValue.toInt
            val routeRefs = analysis.routeReferences.filter(rr => rr.networkType == scopedNetworkType.networkType && rr.networkScope == scopedNetworkType.networkScope).map(_.toRef)
            Some(
              NodeIntegrityDetail(
                scopedNetworkType.networkType,
                scopedNetworkType.networkScope,
                expectedRouteCount,
                routeRefs
              )
            )
          }
          else {
            None
          }
      }
    }
    val integrity = if (nodeIntegrityDetails.nonEmpty) {
      Some(NodeIntegrity(nodeIntegrityDetails))
    }
    else {
      None
    }

    analysis.copy(integrity = integrity)
  }
}
