package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.server.analyzer.engine.context.ElementIds

class NetworkDeleteTest02 extends AbstractIntegrationTest {

  test("network delete - no info saved when before sitution cannot be loaded") {

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, OverpassData.empty, OverpassData.empty)

      tc.process(ChangeAction.Delete, newRawRelation(1))

      assert(!tc.analysisContext.watched.networks.contains(1))

      assert(database.networks.isEmpty)
      assert(database.networkInfos.isEmpty)
      assert(database.changeSetSummaries.isEmpty)
      assert(database.networkInfoChanges.isEmpty)
      assert(database.routeChanges.isEmpty)
    }
  }
}
