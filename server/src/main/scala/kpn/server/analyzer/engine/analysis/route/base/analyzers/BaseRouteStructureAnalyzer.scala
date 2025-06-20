package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Fact
import kpn.api.common.Fact.RouteAnalysisFailed
import kpn.api.common.Fact.RouteNodeMissingInWays
import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotContinious
import kpn.api.common.Fact.RouteNotForward
import kpn.api.common.Fact.RouteNotOneWay
import kpn.api.common.Fact.RouteOneWay
import kpn.api.common.Fact.RouteUnusedSegments
import kpn.api.common.Fact.RouteWithoutNodes
import kpn.api.common.route.LinkDirection

import scala.collection.mutable.ListBuffer

object BaseRouteStructureAnalyzer extends BaseRouteAnalyzer {
  def analyze(context: BaseRouteAnalysisContext): BaseRouteAnalysisContext = {
    new BaseRouteStructureAnalyzer(context).analyze
  }
}

class BaseRouteStructureAnalyzer(context: BaseRouteAnalysisContext) {

  private val facts: ListBuffer[Fact] = ListBuffer[Fact]()
  facts ++= context.facts

  def analyze: BaseRouteAnalysisContext = {

    val structure = {
      if (context.nodeNetwork) {
        new NodeNetworkStructureAnalyzer(context).analyze()
      }
      else {
        new NonNodeNetworkStructureAnalyzer(context).analyze()
      }
    }

    if (!Seq(RouteAnalysisFailed, RouteWithoutNodes, RouteNodeMissingInWays).exists(context.facts.contains)) {

      if (structure.forwardPath.isDefined) {
        if (structure.backwardPath.isDefined) {
          if (context.oneWayRouteForward || context.oneWayRouteBackward) {
            facts += RouteNotOneWay
          }
        }
        else {
          if (!isSingleWayRoundabout) {
            if (context.oneWayRouteForward) {
              facts += RouteOneWay
            }
            else {
              facts += RouteNotBackward
            }
          }
        }
      }
      else if (structure.backwardPath.isDefined) {
        if (context.oneWayRouteBackward) {
          facts += RouteOneWay
        }
        else {
          facts += RouteNotForward
        }
      }
      else {
        if (context.oneWayRouteForward || context.oneWayRouteBackward) {
          facts += RouteNotOneWay
        }
        facts += RouteNotForward
        facts += RouteNotBackward
      }

      if (!Seq(RouteNodeMissingInWays, RouteOneWay).exists(facts.contains)) {
        if (!isSingleWayRoundabout) {
          if (structure.forwardPath.isEmpty || /* segmentAnalysis.structure.forwardPath.get.broken ||*/
            structure.backwardPath.isEmpty /*|| segmentAnalysis.structure.backwardPath.get.broken*/ ) {
            facts += RouteNotContinious
          }
        }
      }

      if (!Seq(RouteNotForward, RouteNotBackward).exists(facts.contains)) {
        if (structure.otherPaths.nonEmpty) {
          facts += RouteUnusedSegments
        }
      }
    }

    context.copy(
      _structure = Some(structure),
      facts = facts.toSeq,
    )
  }

  private def isSingleWayRoundabout: Boolean = {
    if (context.analysisSegments.sizeIs == 1) {
      val elements = context.analysisSegments.head.elements
      if (elements.sizeIs == 1) {
        val fragments = elements.head.fragments
        if (fragments.sizeIs == 1) {
          return fragments.head.link.direction == LinkDirection.RoundaboutRight
        }
      }
    }
    false
  }
}
