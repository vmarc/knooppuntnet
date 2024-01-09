package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.NetworkFact
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
    val rawData = new Parser(full = false).parse(xml)

    val dataBefore = OverpassData(rawData.nodes, rawData.ways, rawData.relations)
    val dataAfter = dataBefore

    simulate(dataBefore, dataAfter) {
      val networkInfo = database.networkInfos.findById(12280062L).get

      networkInfo.facts.shouldMatchTo {
        Seq(
          NetworkFact(
            "NetworkExtraMemberNode",
            Some("node"),
            Some(
              Seq(
                475576273L,
                1523863559L,
                1922278067L,
                3751336934L,
                9331648365L
              )
            ),
            None,
            None
          )
        )
      }
    }
  }
}
