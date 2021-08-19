package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.custom.Tags
import kpn.core.test.OverpassData

class OrphanNodeDeleteTest05 extends IntegrationTest {

  test("ignore delete in foreign country") {

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

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, dataBefore.rawNodeWithId(1001))

      assert(!watched.nodes.contains(1001))
      assert(database.nodes.isEmpty)
      assert(database.nodeChanges.isEmpty)
      assert(database.changes.isEmpty)
    }
  }
}
