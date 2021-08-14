package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData

class OrphanNodeDeleteTest03 extends IntegrationTest {

  test("delete orphan node, 'before' situation not in overpass") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData.empty

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawNode(1001))

      assert(!watched.nodes.contains(1001))

      assert(database.nodes.isEmpty)
      assert(database.changeSetSummaries.isEmpty)
      assert(database.nodeChanges.isEmpty)
    }
  }
}
