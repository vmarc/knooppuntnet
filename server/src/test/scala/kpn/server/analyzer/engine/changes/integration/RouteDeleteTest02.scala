package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class RouteDeleteTest02 extends AbstractIntegrationTest with SharedTestObjects {

  test("delete orphan route, and 'before' situation cannot be found in overpass database") {

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, OverpassData.empty, OverpassData.empty)

      tc.process(ChangeAction.Delete, newRawRelation(11))

      assert(!tc.analysisContext.watched.routes.contains(11))

      assert(database.routes.findAll().isEmpty)
      assert(database.changeSetSummaries.findAll().isEmpty)
      assert(database.routeChanges.findAll().isEmpty)
    }
  }
}
