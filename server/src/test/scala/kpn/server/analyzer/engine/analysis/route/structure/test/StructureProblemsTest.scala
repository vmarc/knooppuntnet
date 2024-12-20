package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy

class StructureProblemsTest extends UnitTest {

  test("2742 RouteSegmentAnalyzer") {
    val context = CaseStudy.analyze("2742")
  }

  test("108126 RouteSegmentAnalyzer") {
    val context = CaseStudy.analyze("108126")
  }

  test("67311 RouteDetailAnalysisContext.scala:113 PreconditionMissingException: null") {
    val context = CaseStudy.analyze("67311")
  }
}
