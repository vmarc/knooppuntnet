package kpn.server.analyzer.engine.analysis.route

import kpn.api.common.route.RoutePath
import kpn.api.common.route.RouteSegment
import kpn.core.doc.RouteDetailDoc
import kpn.core.doc.RouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.analyzers.RouteDocAnalyzer
import kpn.server.analyzer.engine.analysis.route.domain.RouteDocAnalysisContext
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class RouteMainAnalyzer {

  def analyze(routeDetailDoc: RouteDetailDoc): Option[RouteDoc] = {
    Log.context("route=%07d".format(routeDetailDoc.summary.id)) {
      val context = RouteDocAnalysisContext(routeDetailDoc)
      val analyzers: List[RouteDocAnalyzer] = List(
        // RouteLabelsAnalyzer, // this always should be the last analyzer
      )
      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[RouteDocAnalyzer], context: RouteDocAnalysisContext): Option[RouteDoc] = {
    if (analyzers.isEmpty) {
      val segments = context.routeDetailDoc.segments.map { segment =>
        RouteSegment(
          segment.id,
          segment.startNodeId,
          segment.endNodeId,
          segment.meters,
          segment.bounds,
          segment.elementIds
        )
      }

      val paths = context.routeDetailDoc.paths.map { path =>
        RoutePath(
          path.id,
          path.name,
          path.elementIds
        )
      }

      Some(
        RouteDoc(
          context.routeDetailDoc._id, // routeId
          context.routeDetailDoc.labels,
          context.routeDetailDoc.summary,
          context.routeDetailDoc.proposed,
          context.routeDetailDoc.version,
          context.routeDetailDoc.changeSetId,
          context.routeDetailDoc.lastUpdated,
          context.routeDetailDoc.lastSurvey,
          context.routeDetailDoc.facts,
          context.routeDetailDoc.oldFacts,
          context.routeDetailDoc.unexpectedNodeIds,
          context.routeDetailDoc.unexpectedRelationIds,
          context.routeDetailDoc.members,
          context.routeDetailDoc.nameDerivedFromNodes,
          context.routeDetailDoc.nodes,
          context.routeDetailDoc.analysis,
          segments,
          paths
        )
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
