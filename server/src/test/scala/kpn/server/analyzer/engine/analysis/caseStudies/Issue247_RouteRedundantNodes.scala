package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class Issue247_RouteRedundantNodes extends UnitTest {

  test("redundant nodes") {
    pendingRedesign()
    // status 2021-12-20T21:00:00Z
    val route = CaseStudy.baseRouteDoc("13569497")
    route.facts shouldBe empty
    route.summary.name should equal("?-?")
    assertEqual(
      route.summary.tags,
      Tags.from(
        "ref" -> "? - ?",
        "network" -> "lwn",
        "network:type" -> "node_network",
        "route" -> "hiking",
        "type" -> "route"
      )
    )
  }
}
