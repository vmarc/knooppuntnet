package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.UnitTest

class RouteUnexpectedNode extends UnitTest {

  test("no RouteUnexpectedNode for tourism=information + information=board") {
    val route = CaseStudy.routeAnalysis("7645863").route
    route.facts.contains(Fact.RouteUnexpectedNode) should equal(false)
  }

}
