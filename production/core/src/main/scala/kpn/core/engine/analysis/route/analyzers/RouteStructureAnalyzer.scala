package kpn.core.engine.analysis.route.analyzers

import kpn.core.engine.analysis.route.RouteNodeAnalysis
import kpn.core.engine.analysis.route.RouteSortingOrderAnalyzer
import kpn.core.engine.analysis.route.RouteStructure
import kpn.core.engine.analysis.route.domain.RouteAnalysisContext
import kpn.core.engine.analysis.route.segment.Fragment
import kpn.core.engine.analysis.route.segment.SegmentAnalyzer
import kpn.core.engine.analysis.route.segment.SegmentBuilder
import kpn.core.engine.analysis.route.segment.SegmentFinderAbort
import kpn.shared.Fact
import kpn.shared.Fact.RouteAnalysisFailed
import kpn.shared.Fact.RouteIncomplete
import kpn.shared.Fact.RouteNodeMissingInWays
import kpn.shared.Fact.RouteNotBackward
import kpn.shared.Fact.RouteNotForward
import kpn.shared.Fact.RouteOneWay
import kpn.shared.Fact.RouteOverlappingWays
import kpn.shared.Fact.RouteWithoutWays
import kpn.shared.Fact.RouteNotOneWay
import kpn.shared.Fact.RouteNotContinious
import kpn.shared.Fact.RouteUnusedSegments
import kpn.shared.Fact.RouteInvalidSortingOrder

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
          context.interpreter.networkType,
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

          val routeForward = structure.forwardSegment.nonEmpty && !structure.forwardSegment.get.broken
          val routeBackward = structure.backwardSegment.nonEmpty && !structure.backwardSegment.get.broken

          if (routeForward) {
            if (routeBackward) {
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
          else if (routeBackward) {
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
          if (structure.forwardSegment.isEmpty || structure.forwardSegment.get.broken ||
            structure.backwardSegment.isEmpty || structure.backwardSegment.get.broken) {
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
