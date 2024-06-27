package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.custom.Fact
import kpn.core.util.GeoJsonUtil
import kpn.core.util.Redesign
import kpn.core.util.UnitTest

class SebastianTest02 extends UnitTest {

  test("route 22-56") {
    val analysis = CaseStudy.routeAnalysis("11527464")
    analysis.routeDetail.analysis.map.unusedSegments.size should equal(1)
    analysis.routeDetail.oldFacts should equal(Seq(Fact.RouteUnusedSegments, Fact.RouteBroken))
    if (Redesign.enableNewFactTests) {
      analysis.routeDetail.facts should equal(Seq(Fact.RouteUnusedSegments, Fact.RouteBroken))
    }

    analysis.structure.unusedSegments.zipWithIndex.foreach { case (segment, index) =>
      val wayIds = segment.fragments.map(_.fragment.way.id).mkString(", ")
      println(s"segment ${index + 1}, ways: $wayIds")
    }
  }

  test("route 22-56 adapted") {
    val analysis = CaseStudy.routeAnalysis("11527464-adapted")
    GeoJsonUtil.printMap(analysis.routeDetail.analysis.map)
  }
}
