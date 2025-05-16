package kpn.server.analyzer.engine.changes.node.base

import kpn.api.common.Fact
import kpn.api.common.SharedTestObjects
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class BaseNodeChangeFactAnalyzerTest extends UnitTest with SharedTestObjects {

  test("no facts") {
    val before = newNode()
    val after = newNode()
    assertEqual(
      BaseNodeChangeFactAnalyzer.facts(before, after),
      Seq.empty
    )
  }

  test("lost node tag") {

    def doTestLostNodeTag(tagKey: String, expectedFact: Fact): Unit = {
      val before = newNode(tags = Tags.from("network:type" -> "node_network", tagKey -> "01"))
      val after = newNode()
      assertEqual(
        BaseNodeChangeFactAnalyzer.facts(before, after),
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
    val before = newNode(tags = Tags.from("network:type" -> "node_network", "rwn_name" -> "name"))
    val after = newNode(tags = Tags.from("network:type" -> "node_network", "lwn_name" -> "name"))
    assertEqual(
      BaseNodeChangeFactAnalyzer.facts(before, after),
      Seq.empty
    )
  }
}
