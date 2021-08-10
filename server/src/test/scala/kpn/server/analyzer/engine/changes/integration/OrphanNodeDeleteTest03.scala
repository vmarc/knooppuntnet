package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeDeleteTest03 extends AbstractTest {

  test("delete orphan node, 'before' situation not in overpass and also not in mongodb") {

    withDatabase { database =>

      val dataBefore = OverpassData.empty
      val dataAfter = OverpassData.empty

      val tc = new TestContext(database, dataBefore, dataAfter)

      tc.analysisContext.data.nodes.watched.add(1001)

      tc.process(ChangeAction.Delete, newRawNode(1001))

      assert(!tc.analysisContext.data.nodes.watched.contains(1001))

      assert(database.nodes.findAll().isEmpty)
      assert(database.changeSetSummaries.findAll().isEmpty)
      assert(database.nodeChanges.findAll().isEmpty)
    }
  }
}
