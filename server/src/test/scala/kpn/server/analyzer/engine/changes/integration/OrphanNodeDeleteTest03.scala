package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeDeleteTest03 extends AbstractIntegrationTest {

  test("delete orphan node, 'before' situation not in overpass") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData.empty

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Delete, newRawNode(1001))

      assert(!tc.analysisContext.watched.nodes.contains(1001))

      assert(database.nodes.isEmpty)
      assert(database.changeSetSummaries.isEmpty)
      assert(database.nodeChanges.isEmpty)
    }
  }
}
