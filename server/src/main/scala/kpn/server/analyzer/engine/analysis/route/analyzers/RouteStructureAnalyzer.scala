package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteNodeMissingInWays
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.api.custom.Fact.RouteNotOneWay
import kpn.api.custom.Fact.RouteOneWay
import kpn.api.custom.Fact.RouteUnusedSegments
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.context.PreconditionMissingException

import scala.collection.mutable.ListBuffer

object RouteStructureAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new RouteStructureAnalyzer(context).analyze
  }
}

class RouteStructureAnalyzer(context: RouteAnalysisContext) {

  private val facts: ListBuffer[Fact] = ListBuffer[Fact]()
  facts ++= context.facts

  def analyze: RouteAnalysisContext = {

    val segmentAnalysis = context.segmentAnalysis.getOrElse(throw new PreconditionMissingException)

    val oneWayRouteForward = context.relation.hasTag("direction", "forward")
    val oneWayRouteBackward = context.relation.hasTag("direction", "backward")

    val oneWayRoute = context.relation.tags.exists { tag =>
      (tag.key == "comment" && tag.value.contains("to be used in one direction")) ||
        (tag.key == "oneway" && tag.value == "yes") ||
        (tag.key == "signed_direction" && tag.value == "yes")
    }

    val hasValidForwardPath = segmentAnalysis.structure.forwardPath.isDefined // TODO redesign && !structure.forwardPath.exists(_.broken)
    val hasValidBackwardPath = segmentAnalysis.structure.backwardPath.isDefined // TODO redesign && !structure.backwardPath.exists(_.broken)

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
      if (segmentAnalysis.structure.forwardPath.isEmpty || /* segmentAnalysis.structure.forwardPath.get.broken ||*/
        segmentAnalysis.structure.backwardPath.isEmpty /*|| segmentAnalysis.structure.backwardPath.get.broken*/ ) {
        facts += RouteNotContinious
      }
    }

    if (!Seq(RouteNotForward, RouteNotBackward).exists(facts.contains)) {
      if (segmentAnalysis.structure.otherPaths.nonEmpty) {
        facts += RouteUnusedSegments
      }
    }

    context.copy(
      facts = facts.toSeq,
    )
  }
}
