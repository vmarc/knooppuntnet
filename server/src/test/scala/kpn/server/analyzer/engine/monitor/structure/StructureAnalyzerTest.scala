package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.caseStudies.CaseStudy

class StructureAnalyzerTest extends UnitTest {

  ignore("case study") {
    val relation = CaseStudy.load("/case-studies/monitor/4840541.xml")
    println(s"relation.members.size=${relation.members.size}")
    val elementGroups = StructureElementAnalyzer.analyze(relation.members)
    println(s"elementGroups.size=${elementGroups.size}")
    val wayInfos = new ReferenceStructureAnalyzer().analyze(relation)
    wayInfos.foreach(println)
    new StructureAnalyzer().analyze(relation)
    elementGroups.size should equal(1)
  }
}
