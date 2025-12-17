package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class Issue247_RouteRedundantNodes extends UnitTest {

  test("no redundant nodes") {

    // status 2021-12-20T21:00:00Z
    val route = CaseStudy.baseRouteDoc("13569497")

    route.facts shouldNot contain(Fact.RouteRedundantNodes)

    route.base.nodes.startNode.map(_.alternateName) should equal(Some("?.a"))
    route.base.nodes.startTentacleNodes.map(_.alternateName) should equal(Seq("?.b"))
    route.base.nodes.endNode should equal(None)

    route.base.summary.name should equal("?-?")

    assertEqual(
      route.base.raw.tags,
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
