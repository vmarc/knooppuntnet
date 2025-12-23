package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.Relation
import kpn.api.common.RouteScope
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newRelation
import kpn.core.util.UnitTest

class BaseRouteScopeAnalyzerTest extends UnitTest {

  test("scope") {
    val relation = newRelation(
      tags = Tags.from(
        "network" -> "rwn"
      )
    )
    val context = analyze(relation)
    context.scopes should equal(Seq(RouteScope.regional))
  }

  test("multiple scopes") {
    val relation = newRelation(
      tags = Tags.from(
        "network" -> "rwn;lcn"
      )
    )
    val context = analyze(relation)
    context.scopes should equal(Seq(RouteScope.local, RouteScope.regional))
  }

  test("unsupported scope value") {
    val relation = newRelation(
      tags = Tags.from(
        "network" -> "bla"
      )
    )
    val context = analyze(relation)
    context.scopes should equal(Seq(RouteScope.unknown))
  }

  test("no scope when no 'network' tag") {
    val relation = newRelation()
    val context = analyze(relation)
    context.scopes should equal(Seq(RouteScope.unknown))
  }

  private def analyze(relation: Relation): BaseRouteAnalysisContext = {
    val context = BaseRouteAnalysisContext(
      relation,
      None
    )
    BaseRouteScopeAnalyzer.analyze(context)
  }
}
