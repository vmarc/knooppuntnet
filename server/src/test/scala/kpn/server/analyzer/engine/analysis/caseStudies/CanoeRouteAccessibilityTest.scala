package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.Matchers

class CanoeRouteAccessibilityTest extends FunSuite with Matchers {
  test("canoe route accessible") {
    val route = CaseStudy.routeAnalysis("8473146").route
    route.facts should equal(Seq())
  }
}
