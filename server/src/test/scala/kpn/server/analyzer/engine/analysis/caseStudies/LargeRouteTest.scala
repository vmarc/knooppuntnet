package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.Matchers

class LargeRouteTest extends FunSuite with Matchers {

  ignore("Super large route") {
    val analysis = CaseStudy.routeAnalysis("222560")
    analysis.route.analysis.get.map.unusedSegments.size should equal(200)
  }
}
