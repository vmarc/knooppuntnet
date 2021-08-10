package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeCreateTest03 extends AbstractIntegrationTest {

  test("ignore node create in unsupported country") {

    withDatabase { database =>

      val dataBefore = OverpassData.empty
      val dataAfter = OverpassData().node(
        1001,
        latitude = "999",
        longitude = "999",
        tags = Tags.from(
          "proposed:rwn_ref" -> "01",
          "network:type" ->
            "node_network"
        )
      )

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Create, dataAfter.rawNodeWithId(1001))

      assert(!tc.analysisContext.data.nodes.watched.contains(1001))

      assert(database.nodes.findAll().isEmpty)
      assert(database.nodeChanges.findAll().isEmpty)
      assert(database.changeSetSummaries.findAll().isEmpty)
      assert(database.locationChangeSetSummaries.findAll().isEmpty)
    }
  }
}
