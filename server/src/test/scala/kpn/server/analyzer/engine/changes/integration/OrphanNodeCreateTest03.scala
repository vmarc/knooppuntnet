package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.custom.Tags
import kpn.core.test.OverpassData

class OrphanNodeCreateTest03 extends IntegrationTest {

  test("ignore node create in unsupported country") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData().node(
      1001,
      version = 1,
      latitude = "999",
      longitude = "999",
      tags = Tags.from(
        "proposed:rwn_ref" -> "01",
        "network:type" ->
          "node_network"
      )
    )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Create, dataAfter.rawNodeWithId(1001))

      assert(!watched.nodes.contains(1001))

      assert(database.nodes.isEmpty)
      assert(database.nodeChanges.isEmpty)
      assert(database.changes.isEmpty)
    }
  }
}
