package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteNodeMissingInWays
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.api.custom.Fact.RouteNotOneWay
import kpn.api.custom.Fact.RouteOneWay
import kpn.api.custom.Fact.RouteUnusedSegments
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

    val oneWayRouteForward = context.relation.hasTag("direction", "forward")
    val oneWayRouteBackward = context.relation.hasTag("direction", "backward")

    val oneWayRoute = context.relation.hasTag("oneway", "yes") || context.relation.hasTag("signed_direction", "yes")

    val hasValidForwardPath = structure.forwardPath.isDefined // TODO redesign && !structure.forwardPath.exists(_.broken)
    val hasValidBackwardPath = structure.backwardPath.isDefined // TODO redesign && !structure.backwardPath.exists(_.broken)

    if (hasValidForwardPath) {
      if (hasValidBackwardPath) {
        if (oneWayRoute || oneWayRouteForward || oneWayRouteBackward) {
          facts += RouteNotOneWay
        }
      }
      else {
        if (oneWayRoute || oneWayRouteForward) {
          facts += RouteOneWay
        }
        else {
          facts += RouteNotBackward
        }
      }
    }
    else if (hasValidBackwardPath) {
      if (oneWayRoute || oneWayRouteBackward) {
        facts += RouteOneWay
      }
      else {
        facts += RouteNotForward
      }
    }
    else {
      if (oneWayRoute || oneWayRouteForward || oneWayRouteBackward) {
        facts += RouteNotOneWay
      }
      facts += RouteNotForward
      facts += RouteNotBackward
    }

    if (!Seq(RouteNodeMissingInWays, RouteOneWay).exists(facts.contains)) {
      if (structure.forwardPath.isEmpty || /* segmentAnalysis.structure.forwardPath.get.broken ||*/
        structure.backwardPath.isEmpty /*|| segmentAnalysis.structure.backwardPath.get.broken*/ ) {
        facts += RouteNotContinious
      }
    }

    if (!Seq(RouteNotForward, RouteNotBackward).exists(facts.contains)) {
      if (structure.otherPaths.nonEmpty) {
        facts += RouteUnusedSegments
      }
    }

    context.copy(
      _structure = Some(structure),
      facts = facts.toSeq,
    )
  }
}
