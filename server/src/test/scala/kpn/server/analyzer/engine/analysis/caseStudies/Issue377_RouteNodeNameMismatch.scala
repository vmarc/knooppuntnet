package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.core.util.UnitTest

class Issue377_RouteNodeNameMismatch extends UnitTest {

  test("RouteNodeNameMismatch") {
    val route = CaseStudy.baseRouteDoc("13945193")
    route.summary.name should equal("?-?") // derived from ref tag
    route.nodes.startNode.map(_.name) should equal(Some("?"))
    route.nodes.endNode.map(_.name) should equal(Some("La Léchère"))
    route.facts should equal(Seq(Fact.RouteNodeNameMismatch))
  }

  test("route with RouteNodeNameMismatch") {
    val route = CaseStudy.baseRouteDoc("12347801")
    route.summary.name should equal("toto") // derived from name tag
    route.nodes.startNode.map(_.name) should equal(Some("Parking Télécabine"))
    route.nodes.endNode.map(_.name) should equal(Some("Pont de la Bronsonnière"))
    route.facts should equal(Seq(Fact.RouteNodeNameMismatch))
  }
}
