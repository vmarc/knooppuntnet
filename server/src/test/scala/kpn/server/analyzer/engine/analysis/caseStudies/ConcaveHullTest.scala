package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.core.data.DataBuilder
import kpn.core.loadOld.Parser
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.network.NetworkShapeAnalyzer
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzerImpl
import kpn.server.analyzer.engine.context.AnalysisContext

import scala.xml.InputSource
import scala.xml.XML

class ConcaveHullTest extends UnitTest {

  test("reproduce the problem with the concave hull calculation (currently commented out)") {

    val stream = getClass.getResourceAsStream("/case-studies/network-4257206.xml")
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)

    val rawData = new Parser().parse(xml)

    val data = new DataBuilder(rawData).data
    val networkRelation = data.relations(4257206)

    val analysisContext = new AnalysisContext()
    val relationAnalyzer = new RelationAnalyzerImpl(analysisContext)
    new NetworkShapeAnalyzer(relationAnalyzer, networkRelation).shape
  }

}
