package kpn.server.analyzer.engine.analysis.caseStudies

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers

class Issue42_RouteSegmentSourceProblemTest extends AnyFunSuite with Matchers {

  test("segment problem") {
    val analysis = CaseStudy.routeAnalysis("9499242").route.analysis.get

    val forwardPath = analysis.map.forwardPath.get
    analysis.startNodes.head.lat should equal(forwardPath.segments.head.source.lat)
    analysis.startNodes.head.lon should equal(forwardPath.segments.head.source.lon)

    val backwardPath = analysis.map.backwardPath.get
    analysis.endNodes.head.lat should equal(backwardPath.segments.head.source.lat)
    analysis.endNodes.head.lon should equal(backwardPath.segments.head.source.lon)
  }
}
