package kpn.server.analyzer.engine.changes.node

import kpn.api.common.SharedTestObjects
import kpn.api.custom.Fact
import kpn.api.custom.Tags
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.context.AnalysisContext

class NodeChangeFactAnalyzerTest extends UnitTest with SharedTestObjects {

  test("no facts") {
    val context = new AnalysisContext()
    val before = newRawNode()
    val after = newRawNode()

    val analyzer = new NodeChangeFactAnalyzer(context)
    analyzer.facts(before, after) shouldBe empty
  }

  test("lost node tag") {

    def doTestLostNodeTag(tagKey: String, expectedFact: Fact): Unit = {
      val context = new AnalysisContext()
      val before = newRawNode(tags = Tags.from("network:type" -> "node_network", tagKey -> "01"))
      val after = newRawNode()

      val analyzer = new NodeChangeFactAnalyzer(context)
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

    doTestLostNodeTag("rwn_name", Fact.LostHikingNodeTag)
    doTestLostNodeTag("lwn_name", Fact.LostHikingNodeTag)
    doTestLostNodeTag("iwn_name", Fact.LostHikingNodeTag)
    doTestLostNodeTag("rcn_name", Fact.LostBicycleNodeTag)
    doTestLostNodeTag("rhn_name", Fact.LostHorseNodeTag)
    doTestLostNodeTag("rmn_name", Fact.LostMotorboatNodeTag)
    doTestLostNodeTag("rpn_name", Fact.LostCanoeNodeTag)
    doTestLostNodeTag("rin_name", Fact.LostInlineSkateNodeTag)
  }

  test("no lost node tag fact when switching network scope only") {

    val context = new AnalysisContext()
    val before = newRawNode(tags = Tags.from("network:type" -> "node_network", "rwn_name" -> "name"))
    val after = newRawNode(tags = Tags.from("network:type" -> "node_network", "lwn_name" -> "name"))

    val analyzer = new NodeChangeFactAnalyzer(context)
    analyzer.facts(before, after) shouldBe empty
  }

  test("Orphan node that remains orphan") {
    val context = new AnalysisContext()
    context.watched.nodes.add(1001)
    val before = newRawNodeWithName(1001, "01")
    val after = newRawNodeWithName(1001, "01")

    val analyzer = new NodeChangeFactAnalyzer(context)
    analyzer.facts(before, after) shouldBe empty
  }
}
