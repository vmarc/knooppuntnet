package kpn.server.analyzer.engine.analysis.node.analyzers

import kpn.api.common.Fact
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.ScopedRouteType
import kpn.server.analyzer.engine.analysis.node.domain.NodeAnalysis

object NodeIntegrityAnalyzer extends NodeAspectAnalyzer {
  def analyze(analysis: NodeAnalysis): NodeAnalysis = {
    new NodeIntegrityAnalyzer(analysis).analyze
  }
}

class NodeIntegrityAnalyzer(analysis: NodeAnalysis) {

  def analyze: NodeAnalysis = {

    var unexpectedExpectedRouteRelationsTag: Boolean = false

    val nodeIntegrityDetails = ScopedRouteType.all.flatMap { scopedRouteType =>
      analysis.node.tagValue(scopedRouteType.expectedRouteRelationsTag) match {
        case None => None
        case Some(expectedRouteRelationsValue) =>
          if (expectedRouteRelationsValue.forall(Character.isDigit)) {
            if (analysis.routeTypes.contains(scopedRouteType.routeType)) {
              val expectedRouteCount = expectedRouteRelationsValue.toInt
              val routeRefs = analysis.routeReferences.filter(rr => rr.routeType == scopedRouteType.routeType && rr.networkScope == scopedRouteType.networkScope).map(_.toRef)
              Some(
                NodeIntegrityDetail(
                  scopedRouteType.routeType,
                  scopedRouteType.networkScope,
                  expectedRouteCount,
                  routeRefs
                )
              )
            }
            else {
              unexpectedExpectedRouteRelationsTag = true
              None
            }
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

    val facts = if (unexpectedExpectedRouteRelationsTag) {
      analysis.facts :+ Fact.UnexpectedIntegrityCheck
    }
    else {
      analysis.facts
    }

    analysis.copy(
      integrity = integrity,
      facts = facts
    )
  }
}
