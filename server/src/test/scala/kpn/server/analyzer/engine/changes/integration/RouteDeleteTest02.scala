package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData

class RouteDeleteTest02 extends IntegrationTest {

  test("delete orphan route, and 'before' situation cannot be found in overpass database") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData.empty

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawRelation(11))

      assert(!watched.routes.contains(11))

      assert(database.routes.isEmpty)
      assert(database.changes.isEmpty)
      assert(database.routeChanges.isEmpty)
    }
  }
}
