package kpn.server.analyzer.engine.analysis.route.base.analyzers

import kpn.api.common.RouteScope
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newRelation
import kpn.core.util.UnitTest

class RouteScopeAnalyzerTest extends UnitTest {

  test("network tag based scope") {
    testNetworkTag("iwn", RouteScope.international)
    testNetworkTag("nwn", RouteScope.national)
    testNetworkTag("rwn", RouteScope.regional)
    testNetworkTag("lwn", RouteScope.local)
    testNetworkTag("bla", RouteScope.unknown)
  }

  test("scope") {
    val context = analyze(
      Tags.from(
        "type" -> "route",
        "network:type" -> "node_network",
        "network" -> "rwn"
      )
    )
    assertEqual(context.scopes, Seq(RouteScope.regional))
  }

  private def testNetworkTag(tagValue: String, expectedScope: RouteScope): Unit = {
    val context = analyze(
      Tags.from(
        "network" -> tagValue
      )
    )
    assertEqual(context.scopes, Seq(expectedScope))
  }

  private def analyze(tags: Seq[Tag]): BaseRouteAnalysisContext = {
    val relation = newRelation(1L, tags = tags)
    val context = BaseRouteAnalysisContext(
      relation,
      None
    )
    BaseRouteScopeAnalyzer.analyze(context)
  }
}
