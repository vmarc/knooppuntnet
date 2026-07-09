package kpn.server.analyzer.engine.analysis.route.main

import kpn.core.doc.BaseRouteDoc
import kpn.core.doc.RouteDoc
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteBoundsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteGapAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteIdsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteLabelsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteNetworkReferencesAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteParentAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteStructureRowsAnalyzer
import kpn.server.analyzer.engine.analysis.route.main.analyzers.RouteSuperSegmentAnalyzer
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
@Profile(Array("analysis"))
class RouteMainAnalyzer(
  boundsAnalyzer: RouteBoundsAnalyzer,
  routeIdsAnalyzer: RouteIdsAnalyzer,
  routeSuperSegmentAnalyzer: RouteSuperSegmentAnalyzer,
  structureRowsAnalyzer: RouteStructureRowsAnalyzer,
  parentAnalyzer: RouteParentAnalyzer,
  networkReferencesAnalyzer: RouteNetworkReferencesAnalyzer,
) {

  def analyze(route: BaseRouteDoc): Option[RouteDoc] = {
    Log.context(f"route=${route._id}%07d") {
      val context = RouteAnalysisContext(route, facts = route.facts)
      val analyzers: List[RouteAnalyzer] = List(
        routeIdsAnalyzer,
        routeSuperSegmentAnalyzer,
        boundsAnalyzer,
        structureRowsAnalyzer,
        RouteGapAnalyzer,
        parentAnalyzer,
        networkReferencesAnalyzer,
        RouteLabelsAnalyzer, // this always should be the last analyzer
      )
      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(analyzers: List[RouteAnalyzer], context: RouteAnalysisContext): Option[RouteDoc] = {
    if (analyzers.isEmpty) {
      Some(
        buildRouteDoc(context)
      )
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }

  private def buildRouteDoc(context: RouteAnalysisContext): RouteDoc = {
    val relationCount = context.structureRows.count(_.relation.nonEmpty)
    val relationLevels = if (context.structureRows.nonEmpty) {
      context.structureRows.map(_.level).max
    }
    else {
      1
    }

    RouteDoc(
      context.route._id, // routeId
      context.route.active,
      context.labels,
      context.route.base,
      context.facts,
      context.unexpectedRelationIds,
      context.segments,
      context.superSegments.map(_.segments.map(_.info.meters).sum).sum,
      context.superSegments,
      context.paths,
      context.routeIds,
      context.structureRows,
      relationCount,
      relationLevels,
      context.parentRoutes,
      context.networkReferences,
      context.bounds,
      None
    )
  }
}
