package kpn.server.analyzer.engine.analysis.route.analyzers

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Tag
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.domain.RouteDetailAnalysisContext

class RouteScopeAnalyzerTest extends UnitTest with SharedTestObjects {

  test("network tag based scope") {
    testNetworkTag("iwn", "international")
    testNetworkTag("nwn", "national")
    testNetworkTag("rwn", "regional")
    testNetworkTag("lwn", "local")
    testNetworkTag("bla", "unknown")
  }

  test("scope") {
    val context = analyze(
      Tags.from(
        "type" -> "route",
        "network:type" -> "node_network",
        "network" -> "rwn"
      )
    )
    assertEqual(context.scopes, Seq("regional")) // international national regional local
  }

  private def testNetworkTag(tagValue: String, expectedScope: String): Unit = {
    val context = analyze(
      Tags.from(
        "network" -> tagValue
      )
    )
    assertEqual(context.scopes, Seq(expectedScope))
  }

  private def analyze(tags: Seq[Tag]): RouteDetailAnalysisContext = {
    val relation = newRelation(1L, tags = tags)
    val context = RouteDetailAnalysisContext(
      relation,
      None
    )
    RouteScopeAnalyzer.analyze(context)
  }
}
