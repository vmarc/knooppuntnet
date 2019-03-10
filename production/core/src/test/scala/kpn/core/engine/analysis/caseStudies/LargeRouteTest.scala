package kpn.core.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.Matchers

class LargeRouteTest extends FunSuite with Matchers {

  ignore("Super large route") {
    val analysis = CaseStudy.routeAnalysis("222560")
    analysis.route.analysis.get.map.unusedPaths.flatMap(_.segments).size should equal(200)
  }
}
