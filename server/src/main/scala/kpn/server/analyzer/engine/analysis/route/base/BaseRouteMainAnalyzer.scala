package kpn.server.analyzer.engine.analysis.route.base

import kpn.api.common.Relation
import kpn.core.doc.RouteRelation
import kpn.core.util.Log
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteContextAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteCountryAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteEdgeAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteElementsAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteExpectedNameAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteFactCombinationAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteFixmeTodoAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteGeometryDigestAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteInaccessibleAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteIncompleteAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteIncompleteOkAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLastSurveyAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLinkAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteLocationAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteMemberAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteNameAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteNodesAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteOneWayAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteProposedAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteScopeAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteSegmentAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteSegmentAnalyzer2
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteStructureAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteSuspiciousWaysAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTagAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTileAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteTypeAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteUnexpectedNodeAnalyzer
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteWithoutWaysAnalyzer
import org.springframework.stereotype.Component

import scala.annotation.tailrec

@Component
class BaseRouteMainAnalyzer(
  countryAnalyzer: BaseRouteCountryAnalyzer,
  locationAnalyzer: BaseRouteLocationAnalyzer,
  tileAnalyzer: BaseRouteTileAnalyzer
) {

  def analyze(
    relation: Relation,
    subRelationTree: Option[RouteRelation],
    traceEnabled: Boolean = false
  ): BaseRouteAnalysisContext = {

    Log.context(f"route=${relation.id}%07d") {

      val context = BaseRouteAnalysisContext(relation, subRelationTree, traceEnabled = traceEnabled)

      val analyzers: List[BaseRouteAnalyzer] = List(
        BaseRouteTagAnalyzer,
        BaseRouteTypeAnalyzer,
        BaseRouteScopeAnalyzer,
        countryAnalyzer, // TODO redesign - support multiple countries?
        BaseRouteProposedAnalyzer,
        BaseRouteWithoutWaysAnalyzer,
        BaseRouteIncompleteAnalyzer,
        BaseRouteFixmeTodoAnalyzer,
        BaseRouteUnexpectedNodeAnalyzer,

        //OldRouteNodeAnalyzer,
        // TODO RouteNameFromNodesAnalyzer,
        BaseRouteSuspiciousWaysAnalyzer, // OK

        BaseRouteLinkAnalyzer,
        BaseRouteNodesAnalyzer,
        BaseRouteNameAnalyzer,
        BaseRouteExpectedNameAnalyzer,
        BaseRouteSegmentAnalyzer,
        BaseRouteOneWayAnalyzer,
        BaseRouteStructureAnalyzer,
        BaseRouteSegmentAnalyzer2,

        BaseRouteMemberAnalyzer,
        BaseRouteInaccessibleAnalyzer,
        BaseRouteGeometryDigestAnalyzer,
        locationAnalyzer,
        BaseRouteIncompleteOkAnalyzer,
        BaseRouteFactCombinationAnalyzer,
        BaseRouteLastSurveyAnalyzer,
        BaseRouteElementsAnalyzer,
        tileAnalyzer,
        BaseRouteEdgeAnalyzer,
        BaseRouteContextAnalyzer // helper to be used during development only
      )

      doAnalyze(analyzers, context)
    }
  }

  @tailrec
  private def doAnalyze(
    analyzers: List[BaseRouteAnalyzer],
    context: BaseRouteAnalysisContext
  ): BaseRouteAnalysisContext = {

    if (context.abort || analyzers.isEmpty) {
      context
    }
    else {
      val newContext = analyzers.head.analyze(context)
      doAnalyze(analyzers.tail, newContext)
    }
  }
}
