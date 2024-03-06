package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class Issue247_RouteRedundantNodes extends UnitTest {

  test("redundant nodes") {
    // status 2021-12-20T21:00:00Z
    val route = CaseStudy.routeAnalysis("13569497").route
    route.facts shouldBe empty
    route.summary.name should equal("?-?")
    route.tags.shouldMatchTo(
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
