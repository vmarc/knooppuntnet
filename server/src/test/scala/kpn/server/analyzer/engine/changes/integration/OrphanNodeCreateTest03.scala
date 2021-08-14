package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeCreateTest03 extends AbstractIntegrationTest {

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

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Create, dataAfter.rawNodeWithId(1001))

      assert(!tc.analysisContext.watched.nodes.contains(1001))

      assert(database.nodes.isEmpty)
      assert(database.nodeChanges.isEmpty)
      assert(database.changeSetSummaries.isEmpty)
      assert(database.locationChangeSetSummaries.isEmpty)
    }
  }
}
