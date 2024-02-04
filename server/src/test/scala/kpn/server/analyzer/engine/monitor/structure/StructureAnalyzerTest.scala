package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy

class StructureAnalyzerTest extends UnitTest {

  test("case study 1") {
    val relation = CaseStudy.load("/case-studies/monitor/4840541.xml")
    // val wayInfos = new ReferenceStructureAnalyzer().analyze(relation)
    // wayInfos.foreach(println)
    new StructureAnalyzer().analyze(relation)
    val elementGroups = StructureElementAnalyzer.analyze(relation.members)
    elementGroups.size should equal(1)
  }

  test("case study 2") {
    val relation = CaseStudy.load("/case-studies/monitor/5444896.xml")
    val wayInfos = new ReferenceStructureAnalyzer().analyze(relation)
    wayInfos.foreach(println)
    // new StructureAnalyzer().analyze(relation)
    val elementGroups = StructureElementAnalyzer.analyze(relation.members, traceEnabled = true)
    elementGroups.size should equal(1)
  }
}
