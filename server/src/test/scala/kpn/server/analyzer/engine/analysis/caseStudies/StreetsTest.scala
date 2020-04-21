package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class StreetsTest extends FunSuite with Matchers {

  test("find street names in ways") {
    val analysis = CaseStudy.routeAnalysis("1029885")
    analysis.route.analysis.get.map.streets should equal(Seq("Laagheideweg", "Lorbaan"))
  }
}
