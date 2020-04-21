package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class RoundaboutTest extends AnyFunSuite with Matchers {

  test("roundabout in the middle") {
    val analysis = CaseStudy.routeAnalysis("1193198")
    analysis.route.facts.isEmpty should equal(true)
  }
}
