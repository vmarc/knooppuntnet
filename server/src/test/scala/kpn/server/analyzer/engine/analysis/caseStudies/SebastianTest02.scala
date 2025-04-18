package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.util.UnitTest

class SebastianTest02 extends UnitTest {

  test("route 22-56") {
    val analysis = CaseStudy.analyze("11527464")
    pendingRedesign()
    //    analysis.routeDetail.analysis.map.unusedSegments.size should equal(1)
    //    analysis.routeDetail.oldFacts should equal(Seq(Fact.RouteUnusedSegments, Fact.RouteBroken))
    //    if (Redesign.enableNewFactTests) {
    //      analysis.routeDetail.facts should equal(Seq(Fact.RouteUnusedSegments, Fact.RouteBroken))
    //    }
    //
    //    analysis.structure.unusedSegments.zipWithIndex.foreach { case (segment, index) =>
    //      val wayIds = segment.fragments.map(_.fragment.way.id).mkString(", ")
    //      println(s"segment ${index + 1}, ways: $wayIds")
    //    }
  }

  test("route 22-56 adapted") {
    val context = CaseStudy.analyze("11527464-adapted")
    pendingRedesign() // GeoJsonUtil.printMap(context.structure)
  }
}
