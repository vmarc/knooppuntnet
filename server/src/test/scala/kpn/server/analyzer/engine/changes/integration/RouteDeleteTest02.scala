package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newRawRelation

class RouteDeleteTest02 extends IntegrationTest {

  test("delete orphan route, and 'before' situation cannot be found in overpass database") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData.empty

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(11))

      watched.nodes.ids shouldNot contain(11)

      database.routes shouldBe empty
      database.changes shouldBe empty
      database.routeChanges shouldBe empty
    }
  }
}
