package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeDeleteTest05 extends AbstractTest {

  test("ignore delete in foreign country") {

    withDatabase { database =>

      val dataBefore = OverpassData()
        .node(
          1001,
          tags = Tags.from(
            "network:type" -> "node_network",
            "rwn_ref" -> "01"
          ),
          "999",
          "999"
        )

      val dataAfter = OverpassData.empty

      val tc = new TestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Delete, dataBefore.rawNodeWithId(1001))

      assert(!tc.analysisContext.data.nodes.watched.contains(1001))
      assert(database.nodes.findAll().isEmpty)
      assert(database.nodeChanges.findAll().isEmpty)
      assert(database.changeSetSummaries.findAll().isEmpty)
    }
  }
}
