package kpn.server.analyzer.engine.analysis.route.main.analyzers

import kpn.core.test.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteAnalysisContext

class RouteIdsAnalyzerTest extends UnitTest with SharedTestObjects {

  test("route id when there is no subRelationTree") {
    val baseRouteDoc = newBaseRouteDoc(newRouteSummary(11L))
    assertEqual(
      RouteIdsAnalyzer.analyze(RouteAnalysisContext(baseRouteDoc)).routeIds,
      Seq(11)
    )
  }

  test("route ids from subRelationTree") {
    val baseRouteDoc = newBaseRouteDoc(
      newRouteSummary(11L),
      subRelationTree = Some(
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
      RouteIdsAnalyzer.analyze(RouteAnalysisContext(baseRouteDoc)).routeIds,
      Seq(
        1,
        2,
        3,
        4
      )
    )
  }
}
