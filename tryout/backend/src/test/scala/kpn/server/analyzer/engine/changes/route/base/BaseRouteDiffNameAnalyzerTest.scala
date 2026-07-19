package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.diff.route.RouteNameDiff
import kpn.core.test.TestObjects.newRelation
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import kpn.server.analyzer.engine.analysis.route.base.analyzers.RouteNameAnalysis
import org.scalamock.stubs.Stubs

class BaseRouteDiffNameAnalyzerTest extends UnitTest with Stubs {

  test("name diff") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis(
          name = Some("01-02"),
        )
      )
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      _routeNameAnalysis = Some(
        RouteNameAnalysis(
          name = Some("02-01"),
        )
      )
    )

    val nameDiff = new BaseRouteDiffNameAnalyzer().analyze(before, after)
    nameDiff should equal(
      Some(
        RouteNameDiff(Some("01-02"), Some("02-01"))
      )
    )
  }
}
