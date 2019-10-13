package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.Matchers

class RoundaboutTest extends FunSuite with Matchers {

  test("roundabout in the middle") {
    val analysis = CaseStudy.routeAnalysis("1193198")
    analysis.route.facts.isEmpty should equal(true)
  }
}
