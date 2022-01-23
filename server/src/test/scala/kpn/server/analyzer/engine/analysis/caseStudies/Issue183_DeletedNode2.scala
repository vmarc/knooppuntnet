package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.changes.ChangeAction
import kpn.api.custom.Fact
import kpn.core.test.OverpassData
import kpn.server.analyzer.engine.changes.integration.IntegrationTest

class Issue183_DeletedNode2 extends IntegrationTest {

  private val deletedNodeId = 8731919671L

  test("rpn node removed in way in rmn route in rmn network") {

    val dataBefore = OverpassData.load("/case-studies/network-8438300-before.xml")
    val dataAfter = OverpassData.load("/case-studies/network-8438300-after.xml")

    simulate(dataBefore, dataAfter) {

      val nodeBefore = findNodeById(deletedNodeId)

      process(ChangeAction.Delete, newRawNode(deletedNodeId))

      val nodeAfter = findNodeById(deletedNodeId)

      assert(nodeBefore.active)
      assert(!nodeAfter.active)

      val nodeChange = findNodeChangeById("123:1:8731919671")
      nodeChange.facts should equal(Seq(Fact.Deleted))
    }
  }
}
