package kpn.server.analyzer.engine.monitor

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy

class MonitorRouteStructureAnalyzerTest extends UnitTest {

  test("relation") {
    val relation = CaseStudy.load("/case-studies/monitor/4840541.xml")
    println(s"relation.members.size=${relation.members.size}")
    new MonitorRouteStructureAnalyzer().analyzeRoute(relation)
  }
}
