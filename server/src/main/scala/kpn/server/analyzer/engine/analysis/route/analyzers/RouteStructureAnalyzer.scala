package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.custom.Fact
import kpn.api.custom.Fact.RouteAnalysisFailed
import kpn.api.custom.Fact.RouteInvalidSortingOrder
import kpn.api.custom.Fact.RouteNodeMissingInWays
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.api.custom.Fact.RouteNotOneWay
import kpn.api.custom.Fact.RouteOneWay
import kpn.api.custom.Fact.RouteOverlappingWays
import kpn.api.custom.Fact.RouteUnusedSegments
import kpn.api.custom.Fact.RouteWithoutWays
import kpn.server.analyzer.engine.analysis.route.RouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteSortingOrderAnalyzer
import kpn.server.analyzer.engine.analysis.route.RouteStructure
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.SegmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.SegmentBuilder
import kpn.server.analyzer.engine.analysis.route.segment.SegmentFinderAbort

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
    val structure = analyzeStructure(context.routeNodeAnalysis.get, context.fragments.get)
    analyzeStructure2(context.routeNodeAnalysis.get, structure, context.fragments.get)
    context.copy(structure = Some(structure), facts = facts.toSeq)
  }

  private def analyzeStructure(routeNodeAnalysis: RouteNodeAnalysis, fragments: Seq[Fragment]): RouteStructure = {

    if (context.connection && !routeNodeAnalysis.hasStartAndEndNode) {
      RouteStructure(
        unusedSegments = new SegmentBuilder().segments(fragments)
      )
    }
    else if (Seq(RouteNodeMissingInWays, RouteOverlappingWays).exists(facts.contains)) {
      RouteStructure(
        unusedSegments = new SegmentBuilder().segments(fragments)
      )
    }
    else if (routeNodeAnalysis.redundantNodes.size > 3) {
      RouteStructure(
        unusedSegments = new SegmentBuilder().segments(fragments)
      )
    }
    else {
      try {
        new SegmentAnalyzer(
          context.networkType,
          context.loadedRoute.relation.id,
          fragments,
          routeNodeAnalysis
        ).structure
      }
      catch {
        case e: SegmentFinderAbort =>

          facts += RouteAnalysisFailed

          RouteStructure()
      }
    }
  }

  private def analyzeStructure2(routeNodeAnalysis: RouteNodeAnalysis, structure: RouteStructure, fragments: Seq[Fragment]): Unit = {
    if (!Seq(RouteAnalysisFailed, RouteOverlappingWays).exists(facts.contains)) {
      if (!context.connection || routeNodeAnalysis.hasStartAndEndNode) {
        if (!facts.contains(RouteWithoutWays)) {
          // do not report this fact if route has no ways or is known to be incomplete

          val oneWayRouteForward = context.loadedRoute.relation.tags.has("direction", "forward")
          val oneWayRouteBackward = context.loadedRoute.relation.tags.has("direction", "backward")

          val oneWayRoute = context.loadedRoute.relation.tags.tags.exists { tag =>
            (tag.key == "comment" && tag.value.contains("to be used in one direction")) ||
              (tag.key == "oneway" && tag.value == "yes")
          }

          val hasValidForwardPath = structure.forwardPath.isDefined && !structure.forwardPath.exists(_.broken)
          val hasValidBackwardPath = structure.backwardPath.isDefined && !structure.backwardPath.exists(_.broken)

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
        }

        if (!Seq(RouteNodeMissingInWays, RouteOneWay).exists(facts.contains)) {
          if (structure.forwardPath.isEmpty || structure.forwardPath.get.broken ||
            structure.backwardPath.isEmpty || structure.backwardPath.get.broken) {
            facts += RouteNotContinious
          }
        }

        if (!Seq(RouteNotForward, RouteNotBackward).exists(facts.contains)) {
          if (structure.unusedSegments.nonEmpty) {
            facts += RouteUnusedSegments
          }

          val routeSortingOrderAnalysis = new RouteSortingOrderAnalyzer(fragments, structure).analysis
          if (!routeSortingOrderAnalysis.ok) {
            facts += RouteInvalidSortingOrder
          }
        }
      }
    }
  }

}
