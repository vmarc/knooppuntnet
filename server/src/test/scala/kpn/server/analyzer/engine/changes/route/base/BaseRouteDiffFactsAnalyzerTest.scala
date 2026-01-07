package kpn.server.analyzer.engine.changes.route.base

import kpn.api.common.Fact
import kpn.api.common.diff.common.FactDiffs
import kpn.core.test.TestObjects.newRelation
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.base.analyzers.BaseRouteAnalysisContext
import org.scalamock.stubs.Stubs

class BaseRouteDiffFactsAnalyzerTest extends UnitTest with Stubs {

  test("fact diff") {

    val before = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      facts = Seq(
        Fact.RouteUnexpectedNode, // resolved
        Fact.RouteInaccessible // remaining
      )
    )

    val after = BaseRouteAnalysisContext(
      newRelation(11),
      None,
      facts = Seq(
        Fact.RouteUnexpectedRelation, // introduced
        Fact.RouteBroken, // introduced
        Fact.RouteInaccessible // remaining
      )
    )

    val factDiffs = new BaseRouteDiffFactsAnalyzer().analyze(before, after)
    factDiffs should equal(
      Some(
        FactDiffs(
          resolved = Seq(
            Fact.RouteUnexpectedNode
          ),
          introduced = Seq(
            Fact.RouteUnexpectedRelation,
            Fact.RouteBroken
          ),
          remaining = Seq(
            Fact.RouteInaccessible
          )
        )
      )
    )
  }
}
