package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class Issue54_OverlappingWays extends FunSuite with Matchers {

  test("fact overlapping ways is disabled") {

    val routeIds = Seq(
      "10015252",
      "9905468",
      "7656918",
      "7630595",
      "8946036",
      "7485964",
      "9445395",
      "7630594",
      "3921598",
      "3921594",
      "9637368",
      "1621989",
      "7175609",
      "2506564",
      "2506564"
    )

    routeIds.foreach { routeId =>
      val route = CaseStudy.routeAnalysis(routeId).route
      route.facts.contains(Fact.RouteOverlappingWays) should equal(false)
    }
  }
}
