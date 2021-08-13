package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.server.analyzer.engine.changes.changes.ElementIds

class NetworkDeleteTest02 extends AbstractIntegrationTest {

  test("network delete - no info saved when before sitution cannot be loaded") {

    pending

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, OverpassData.empty, OverpassData.empty)

      tc.analysisContext.data.networks.watched.add(1, ElementIds())

      tc.process(ChangeAction.Delete, newRawRelation(1))

      assert(!tc.analysisContext.data.networks.watched.contains(1))

      (tc.networkRepository.oldSaveNetworkInfo _).verify(*).never()

      assert(database.changeSetSummaries.findAll().isEmpty)
      assert(database.networkInfoChanges.findAll().isEmpty)
      assert(database.routeChanges.findAll().isEmpty)
    }
  }
}
