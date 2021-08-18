package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.GeoJsonUtil
import kpn.core.util.UnitTest

class SebastianTest01 extends UnitTest {

  test("route 44-53") {
    val analysis = CaseStudy.routeAnalysis("11721562")
    analysis.route.analysis.map.unusedSegments.size should equal(2)
    analysis.route.facts should equal(Seq(Fact.RouteUnusedSegments, Fact.RouteBroken))

    analysis.structure.unusedSegments.zipWithIndex.foreach { case (segment, index) =>
      val wayIds = segment.fragments.map(_.fragment.way.id).mkString(", ")
      println(s"segment ${index + 1}, ways: $wayIds")
    }
  }

  test("route 44-53 adapted") {
    val analysis = CaseStudy.routeAnalysis("11721562-adapted")
    assert(analysis.route.facts.isEmpty)
    GeoJsonUtil.printMap(analysis.route.analysis.map)
  }
}