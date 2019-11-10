package kpn.server.analyzer.engine.changes.node

import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.server.analyzer.engine.changes.data.AnalysisData
import kpn.api.common.SharedTestObjects
import org.scalatest.FunSuite
import org.scalatest.Matchers

class NodeChangeFactAnalyzerTest extends FunSuite with Matchers with SharedTestObjects {

  test("no facts") {
    val analysisData = AnalysisData()
    val before = newRawNode()
    val after = newRawNode()

    val analyzer = new NodeChangeFactAnalyzer(analysisData)
    analyzer.facts(before, after) should equal(
      Seq()
    )
  }

  test("lost node tag") {

    def doTestLostNodeTag(tagKey: String, expectedFact: Fact): Unit = {
      val analysisData = AnalysisData()
      val before = newRawNode(tags = Tags.from(tagKey -> "01", "network:type" -> "node_network"))
      val after = newRawNode()

      val analyzer = new NodeChangeFactAnalyzer(analysisData)
      analyzer.facts(before, after) should equal(
        Seq(expectedFact)
      )
    }

    doTestLostNodeTag("rwn_ref", Fact.LostHikingNodeTag)
    doTestLostNodeTag("lwn_ref", Fact.LostHikingNodeTag)
    doTestLostNodeTag("iwn_ref", Fact.LostHikingNodeTag)
    doTestLostNodeTag("rcn_ref", Fact.LostBicycleNodeTag)
    doTestLostNodeTag("rhn_ref", Fact.LostHorseNodeTag)
    doTestLostNodeTag("rmn_ref", Fact.LostMotorboatNodeTag)
    doTestLostNodeTag("rpn_ref", Fact.LostCanoeNodeTag)
    doTestLostNodeTag("rin_ref", Fact.LostInlineSkateNodeTag)
  }

  test("WasOrphan") {
    val analysisData = AnalysisData()
    analysisData.orphanNodes.watched.add(1)
    val before = newRawNode()
    val after = newRawNode()

    val analyzer = new NodeChangeFactAnalyzer(analysisData)
    analyzer.facts(before, after) should equal(
      Seq(Fact.WasOrphan)
    )
  }

  test("Orphan node that remains orphan") {
    val analysisData = AnalysisData()
    analysisData.orphanNodes.watched.add(1001)
    val before = newRawNodeWithName(1001, "01")
    val after = newRawNodeWithName(1001, "01")

    val analyzer = new NodeChangeFactAnalyzer(analysisData)
    analyzer.facts(before, after) should equal(Seq())
  }

}
