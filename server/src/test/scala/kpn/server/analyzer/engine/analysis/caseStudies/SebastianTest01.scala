package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.core.util.UnitTest

class SebastianTest01 extends UnitTest {

  test("route 44-53") {
    val context = CaseStudy.analyze("11721562")
    context.structure.otherPaths.size should equal(7)
    context.facts should equal(Seq(Fact.RouteUnusedSegments, Fact.RouteBroken))
  }

  test("route 44-53 adapted") {
    val context = CaseStudy.analyze("11721562-adapted")
    assert(context.facts.isEmpty)
    // GeoJsonUtil.printMap(context.structure)
  }
}
