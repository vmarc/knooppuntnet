package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.changes.ChangeAction
import kpn.api.common.node.NodeIntegrity
import kpn.api.common.node.NodeIntegrityDetail
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.core.test.OverpassData
import kpn.server.analyzer.engine.changes.integration.IntegrationTest

class Issue183_DeletedNode3 extends IntegrationTest {

  private val deletedNodeId = 2969204425L

  test("node looses node tags") {

    val dataBefore = OverpassData.load("/case-studies/node-2969204425-before.xml")
    val dataAfter = OverpassData.load("/case-studies/node-2969204425-after.xml")

    simulate(dataBefore, dataAfter) {

      val nodeBefore = findNodeById(deletedNodeId)
      assert(nodeBefore.active)
      nodeBefore.name should equal("Pau49")
      nodeBefore.names should equal(
        Seq(
          newNodeName(
            NetworkType.cycling,
            NetworkScope.regional,
            "Pau49"
          )
        )
      )
      nodeBefore.integrity should equal(
        Some(
          NodeIntegrity(
            Seq(
              NodeIntegrityDetail(
                NetworkType.cycling,
                NetworkScope.regional,
                3,
                Seq.empty
              )
            )
          )
        )
      )

      process(ChangeAction.Modify, newRawNode(deletedNodeId))

      val nodeAfter = findNodeById(deletedNodeId)

      assert(!nodeAfter.active)

      val nodeChange = findNodeChangeById("123:1:2969204425")
      nodeChange.facts should equal(Seq(Fact.Deleted))
    }
  }
}
