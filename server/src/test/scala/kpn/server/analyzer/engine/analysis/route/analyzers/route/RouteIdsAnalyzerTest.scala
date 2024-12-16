package kpn.server.analyzer.engine.analysis.route.analyzers.route

import kpn.api.common.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

class RouteIdsAnalyzerTest extends UnitTest with SharedTestObjects {

  test("route id when there is no hierarchy") {
    val routeDetailDoc = newRouteDetailDoc(newRouteSummary(11L))
    assertEqual(
      RouteIdsAnalyzer.analyze(RouteAnalysisContext(routeDetailDoc)).routeIds,
      Seq(11)
    )
  }

  test("route ids from hierarchy") {
    val routeDetailDoc = newRouteDetailDoc(
      newRouteSummary(11L),
      hierarchy = Some(
        newRouteRelation(
          1,
          relations = Seq(
            newRouteRelation(2),
            newRouteRelation(
              3,
              relations = Seq(
                newRouteRelation(4)
              )
            )
          )
        )
      )
    )

    assertEqual(
      RouteIdsAnalyzer.analyze(RouteAnalysisContext(routeDetailDoc)).routeIds,
      Seq(
        1,
        2,
        3,
        4
      )
    )
  }
}
