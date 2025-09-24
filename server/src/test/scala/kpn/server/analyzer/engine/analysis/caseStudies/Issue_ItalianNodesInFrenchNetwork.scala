package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.Fact
import kpn.core.loadOld.Parser
import kpn.core.test.OverpassData
import kpn.server.analyzer.engine.changes.integration.IntegrationTest

import scala.xml.InputSource
import scala.xml.XML

// reproduce issue reported by StC in telegram message
class Issue_ItalianNodesInFrenchNetwork extends IntegrationTest {

  test("Italian nodes in French network cause NetworkExtraMemberNode fact") {

    val filename = s"/case-studies/12280062.xml"
    val stream = getClass.getResourceAsStream(filename)
    val inputSource = new InputSource(stream)
    val xml = XML.load(inputSource)
    val rawData = new Parser(includeMetadata = false).parse(xml)

    val dataBefore = OverpassData(rawData.nodes, rawData.ways, rawData.relations)
    val dataAfter = dataBefore

    simulate(dataBefore, dataAfter) {
      val network = findNetworkById(12280062L)
      network.facts should not contain Fact.NetworkExtraMemberNode
    }
  }
}
