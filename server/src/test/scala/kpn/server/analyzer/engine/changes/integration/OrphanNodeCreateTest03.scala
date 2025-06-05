package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.custom.Tags
import kpn.core.test.Locations
import kpn.core.test.OverpassData

class OrphanNodeCreateTest03 extends IntegrationTest {

  test("ignore node create in unsupported country") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData().node(
      1001,
      version = 1,
      latitude = Locations.newYork.latitude,
      longitude = Locations.newYork.longitude,
      tags = Tags.from(
        "proposed:rwn_ref" -> "01",
        "network:type" ->
          "node_network"
      )
    )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Create, dataAfter.rawNodeWithId(1001))

      pendingRedesign()

      watched.nodes.ids shouldNot contain(1001)

      database.nodes shouldBe empty
      database.nodeChanges shouldBe empty
      database.changes shouldBe empty
    }
  }
}
