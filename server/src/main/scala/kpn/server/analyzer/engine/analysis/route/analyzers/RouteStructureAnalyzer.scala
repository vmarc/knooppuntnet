package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteAnalysisFailed
import kpn.api.custom.Fact.RouteNodeMissingInWays
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.api.custom.Fact.RouteNotOneWay
import kpn.api.custom.Fact.RouteOneWay
import kpn.api.custom.Fact.RouteUnusedSegments
import kpn.api.custom.Fact.RouteWithoutNodes
import kpn.core.analysis.LinkDirection
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext
import kpn.server.analyzer.engine.analysis.route.structure.StructureAnalyzer

import scala.collection.mutable.ListBuffer

object RouteStructureAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteDetailAnalysisContext): RouteDetailAnalysisContext = {
    new RouteStructureAnalyzer(context).analyze
  }
}

class RouteStructureAnalyzer(context: RouteDetailAnalysisContext) {

  private val facts: ListBuffer[Fact] = ListBuffer[Fact]()
  facts ++= context.facts

  def analyze: RouteDetailAnalysisContext = {

    val structure = new StructureAnalyzer(context).analyze()

    if (!Seq(RouteAnalysisFailed, RouteWithoutNodes, RouteNodeMissingInWays).exists(context.facts.contains)) {

      if (structure.forwardPath.isDefined) {
        if (structure.backwardPath.isDefined) {
          if (context.oneWayRouteForward || context.oneWayRouteBackward) {
            facts += RouteNotOneWay
          }
        }
        else {
          if (!isSingleWayRoundabout()) {
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
        if (!isSingleWayRoundabout()) {
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

  private def isSingleWayRoundabout(): Boolean = {
    if (context.segments.sizeIs == 1) {
      val elements = context.segments.head.elements
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
