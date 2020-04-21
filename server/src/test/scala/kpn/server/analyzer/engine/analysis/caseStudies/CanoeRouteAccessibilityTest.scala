package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class CanoeRouteAccessibilityTest extends AnyFunSuite with Matchers {
  test("canoe route accessible") {
    val route = CaseStudy.routeAnalysis("8473146").route
    route.facts should equal(Seq())
  }
}
