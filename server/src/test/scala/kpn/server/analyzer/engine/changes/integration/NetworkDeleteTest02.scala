package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newRawRelation

class NetworkDeleteTest02 extends IntegrationTest {

  test("network delete - no info saved when before sitution cannot be loaded") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData.empty

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(1))

      watched.networks shouldNot contain(1)

      database.baseNetworks shouldBe empty
      database.networks shouldBe empty
      database.changes shouldBe empty
      database.networkChanges shouldBe empty
      database.routeChanges shouldBe empty
    }
  }
}
