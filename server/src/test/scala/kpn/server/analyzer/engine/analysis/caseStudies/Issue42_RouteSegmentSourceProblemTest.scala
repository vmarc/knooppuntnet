package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class Issue42_RouteSegmentSourceProblemTest extends UnitTest {

  test("segment problem") {
    val analysis = CaseStudy.routeAnalysis("9499242").route.analysis

    val forwardPath = analysis.map.forwardPath.get
    analysis.map.startNodes.head.lat should equal(forwardPath.segments.head.source.lat)
    analysis.map.startNodes.head.lon should equal(forwardPath.segments.head.source.lon)

    val backwardPath = analysis.map.backwardPath.get
    analysis.map.endNodes.head.lat should equal(backwardPath.segments.head.source.lat)
    analysis.map.endNodes.head.lon should equal(backwardPath.segments.head.source.lon)
  }
}
