package kpn.server.analyzer.engine.analysis.route.main

import kpn.api.base.ObjectId
import kpn.core.doc.RouteDetailDoc
import kpn.core.doc.RouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteBoundsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteIdsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteParentAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteStructureRowsAnalyzer
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class RouteMainAnalyzer(
  boundsAnalyzer: RouteBoundsAnalyzer,
  structureRowsAnalyzer: RouteStructureRowsAnalyzer,
  parentAnalyzer: RouteParentAnalyzer
) {

  def analyze(routeDetailDoc: RouteDetailDoc): Option[RouteDoc] = {
    Log.context(f"route=${routeDetailDoc.summary.id}%07d") {
      val context = RouteAnalysisContext(routeDetailDoc)
      val analyzers: List[RouteAnalyzer] = List(
        RouteIdsAnalyzer,
        boundsAnalyzer,
        structureRowsAnalyzer,
        parentAnalyzer,
        // RouteLabelsAnalyzer, // this always should be the last analyzer
      )
      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[RouteAnalyzer], context: RouteAnalysisContext): Option[RouteDoc] = {
    if (analyzers.isEmpty) {

      val summary = context.routeDetailDoc.summary.copy(
        meters = context.distance
      )

      Some(
        RouteDoc(
          context.routeDetailDoc._id, // routeId
          context.routeDetailDoc.labels,
          summary,
          context.routeDetailDoc.proposed,
          context.routeDetailDoc.version,
          context.routeDetailDoc.changeSetId,
          context.routeDetailDoc.lastUpdated,
          context.routeDetailDoc.lastSurvey,
          context.routeDetailDoc.facts,
          context.routeDetailDoc.unexpectedNodeIds,
          context.routeDetailDoc.unexpectedRelationIds,
          context.routeDetailDoc.members,
          context.routeDetailDoc.nameDerivedFromNodes,
          context.routeDetailDoc.nodes,
          context.routeDetailDoc.analysis,
          context.routeDetailDoc.locationAnalysis,
          context.segments,
          context.paths,
          context.routeIds,
          context.bounds,
          context.structureRows,
          context.parentRoutes,
          Some(ObjectId())
        )
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
