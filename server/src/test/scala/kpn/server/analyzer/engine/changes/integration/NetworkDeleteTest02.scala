package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData

class NetworkDeleteTest02 extends IntegrationTest {

  test("network delete - no info saved when before sitution cannot be loaded") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData.empty

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(1))

      assert(!watched.networks.contains(1))

      assert(database.networks.isEmpty)
      assert(database.networkInfos.isEmpty)
      assert(database.changeSetSummaries.isEmpty)
      assert(database.networkInfoChanges.isEmpty)
      assert(database.routeChanges.isEmpty)
    }
  }
}
