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
import kpn.api.custom.Fact.RouteWithoutWays
import kpn.server.analyzer.engine.analysis.route.OldRouteNodeAnalysis
import kpn.server.analyzer.engine.analysis.route.RouteStructure
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.segment.Fragment
import kpn.server.analyzer.engine.analysis.route.segment.SegmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.segment.SegmentBuilder
import kpn.server.analyzer.engine.analysis.route.segment.SegmentFinderAbort

import scala.collection.mutable.ListBuffer

object OldRouteStructureAnalyzer extends RouteAnalyzer {
  def analyze(context: RouteAnalysisContext): RouteAnalysisContext = {
    new OldRouteStructureAnalyzer(context).analyze
  }
}

class OldRouteStructureAnalyzer(context: RouteAnalysisContext) {

  private val oldFacts: ListBuffer[Fact] = ListBuffer[Fact]()
  oldFacts ++= context.oldFacts

  def analyze: RouteAnalysisContext = {
    val structure = analyzeStructure(context.oldRouteNodeAnalysis)
    analyzeStructure2(context.oldRouteNodeAnalysis, structure, context.fragmentMap.all)
    context.copy(
      _structure = Some(structure),
      oldFacts = oldFacts.toSeq,
    )
  }

  private def analyzeStructure(routeNodeAnalysis: OldRouteNodeAnalysis): RouteStructure = {

    if (isAnalysisImpossible(routeNodeAnalysis)) {
      RouteStructure(
        unusedSegments = new SegmentBuilder(context.networkTypes, context.fragmentMap).segments(context.fragmentMap.ids)
      )
    }
    else {
      try {
        new SegmentAnalyzer(
          context.networkTypes,
          context.relation.id,
          context.routeNameAnalysis.isStartNodeNameSameAsEndNodeName,
          context.fragmentMap,
          routeNodeAnalysis
        ).structure
      }
      catch {
        case e: SegmentFinderAbort =>
          oldFacts += RouteAnalysisFailed
          RouteStructure()
      }
    }
  }

  private def analyzeStructure2(routeNodeAnalysis: OldRouteNodeAnalysis, structure: RouteStructure, fragments: Seq[Fragment]): Unit = {
    if (!Seq(RouteAnalysisFailed, RouteWithoutNodes, RouteNodeMissingInWays).exists(oldFacts.contains)) {
      if (!context.connection || routeNodeAnalysis.hasStartAndEndNode) {
        if (!oldFacts.contains(RouteWithoutWays)) {
          // do not report this fact if route has no ways or is known to be incomplete

          if (routeNodeAnalysis.freeNodes.nonEmpty) {
            if (structure.unusedSegments.nonEmpty) {
              oldFacts += RouteUnusedSegments
            }
          }
          else {

            val oneWayRouteForward = context.relation.hasTag("direction", "forward")
            val oneWayRouteBackward = context.relation.hasTag("direction", "backward")

            val oneWayRoute = context.relation.tags.exists { tag =>
              (tag.key == "comment" && tag.value.contains("to be used in one direction")) ||
                (tag.key == "oneway" && tag.value == "yes") ||
                (tag.key == "signed_direction" && tag.value == "yes")
            }

            val hasValidForwardPath = structure.forwardPath.isDefined && !structure.forwardPath.exists(_.broken)
            val hasValidBackwardPath = structure.backwardPath.isDefined && !structure.backwardPath.exists(_.broken)

            if (hasValidForwardPath) {
              if (hasValidBackwardPath) {
                if (oneWayRoute || oneWayRouteForward || oneWayRouteBackward) {
                  oldFacts += RouteNotOneWay
                }
              }
              else {
                if (oneWayRoute || oneWayRouteForward) {
                  oldFacts += RouteOneWay
                }
                else {
                  oldFacts += RouteNotBackward
                }
              }
            }
            else if (hasValidBackwardPath) {
              if (oneWayRoute || oneWayRouteBackward) {
                oldFacts += RouteOneWay
              }
              else {
                oldFacts += RouteNotForward
              }
            }
            else {
              if (oneWayRoute || oneWayRouteForward || oneWayRouteBackward) {
                oldFacts += RouteNotOneWay
              }
              oldFacts += RouteNotForward
              oldFacts += RouteNotBackward
            }

            if (!Seq(RouteNodeMissingInWays, RouteOneWay).exists(oldFacts.contains)) {
              if (structure.forwardPath.isEmpty || structure.forwardPath.get.broken ||
                structure.backwardPath.isEmpty || structure.backwardPath.get.broken) {
                oldFacts += RouteNotContinious
              }
            }

            if (!Seq(RouteNotForward, RouteNotBackward).exists(oldFacts.contains)) {
              if (structure.unusedSegments.nonEmpty) {
                oldFacts += RouteUnusedSegments
              }
            }
          }
        }
      }
    }
  }

  private def isAnalysisImpossible(routeNodeAnalysis: OldRouteNodeAnalysis): Boolean = {
    if (context.connection && !routeNodeAnalysis.hasStartAndEndNode) {
      return true
    }
    if (oldFacts.contains(RouteWithoutNodes)) {
      return true
    }
    if (Seq(RouteNodeMissingInWays).exists(oldFacts.contains)) {
      // TODO ANALYSIS review this rule: RouteNodeMissingInWays means no node in ways at all, or one or more missing ???
      // TODO ANALYSIS review this rule: overlapping ways should not cause the analysis to fail ???
      return true
    }
    false
  }
}
