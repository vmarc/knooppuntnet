package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newRawNode

class OrphanNodeDeleteTest03 extends IntegrationTest {

  test("delete orphan node, 'before' situation not in overpass") {

    val dataBefore = OverpassData.empty
    val dataAfter = OverpassData.empty

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Delete, newRawNode(1001))

      watched.nodes shouldNot contain(1001)

      database.nodes shouldBe empty
      database.changes shouldBe empty
      database.nodeChanges shouldBe empty
    }
  }
}
