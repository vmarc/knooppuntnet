package kpn.server.analyzer.engine.analysis.node.main.analyzers

import kpn.api.common.Fact
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.ScopedRouteType

object NodeIntegrityAnalyzer extends NodeAnalyzer {
  def analyze(context: NodeAnalysisContext): NodeAnalysisContext = {
    new NodeIntegrityAnalyzer(context).analyze
  }
}

class NodeIntegrityAnalyzer(context: NodeAnalysisContext) {

  def analyze: NodeAnalysisContext = {

    var unexpectedExpectedRouteRelationsTag: Boolean = false

    val nodeIntegrityDetails = ScopedRouteType.all.flatMap { scopedRouteType =>
      context.node.tagValue(scopedRouteType.expectedRouteRelationsTag) match {
        case None => None
        case Some(expectedRouteRelationsValue) =>
          if (expectedRouteRelationsValue.forall(Character.isDigit)) {
            if (context.routeTypes.contains(scopedRouteType.routeType)) {
              val expectedRouteCount = expectedRouteRelationsValue.toInt
              val routeRefs = context.routeReferences.filter(rr =>
                rr.routeType == scopedRouteType.routeType &&
                  rr.routeScope == scopedRouteType.routeScope
              ).map(_.toRef)
              Some(
                NodeIntegrityDetail(
                  scopedRouteType.routeType,
                  scopedRouteType.routeScope,
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
      context.facts :+ Fact.UnexpectedIntegrityCheck
    }
    else {
      context.facts
    }

    context.copy(
      _integrity = Some(integrity),
      facts = facts
    )
  }
}
