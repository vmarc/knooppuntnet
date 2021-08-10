package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.SharedTestObjects
import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.server.analyzer.engine.changes.changes.ElementIds

class RouteDeleteTest02 extends AbstractIntegrationTest with SharedTestObjects {

  test("delete orphan route, and 'before' situation cannot be found in overpass database") {

    pending

    withDatabase { database =>
      val tc = new IntegrationTestContext(database, OverpassData.empty, OverpassData.empty)
      tc.analysisContext.data.routes.watched.add(11, ElementIds())

      tc.process(ChangeAction.Delete, newRawRelation(11))

      assert(!tc.analysisContext.data.routes.watched.contains(11))

      (tc.routeRepository.save _).verify(*).never() // too difficult to save meaningful data

      assert(database.changeSetSummaries.findAll().isEmpty)
      assert(database.routeChanges.findAll().isEmpty)
    }
  }
}
