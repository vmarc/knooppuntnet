package kpn.core.engine.changes.integration

import kpn.core.test.TestData2
import kpn.shared.changes.ChangeAction

class NetworkUpdateCollectionTest extends AbstractTest {

  test("network update - when network relation added, the network becomes a network collection") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(1, "name", Seq(newMember("node", 1001)))
      .networkRelation(2, "name", Seq(newMember("node", 1002)))
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkRelation(1, "name", Seq(newMember("node", 1001)))
      .networkRelation(
        2,
        "name",
        Seq(
          newMember("node", 1002),
          newMember("relation", 1)
        )
      )
      .data

    val tc = new TestConfig()
    tc.watchNetwork(dataBefore, 1)
    tc.relationBefore(dataBefore, 2)
    tc.watchNetwork(dataBefore, 2)
    tc.relationAfter(dataAfter, 2)

    tc.process(ChangeAction.Modify, relation(dataAfter, 2))

    tc.analysisData.networks.watched.contains(1) should equal(true)
    tc.analysisData.networks.watched.contains(2) should equal(false)

    tc.analysisData.networks.ignored.contains(1) should equal(false)
    tc.analysisData.networks.ignored.contains(2) should equal(true)

    pending

    // TODO CHANGE assert relation saved as ignored network
    // TODO CHANGE further check for orphan nodes/routes (add routes in test data), networkUpdate object, etc.
    // TODO CHANGE route should not become orphan but also ignored if they are not part of any other network

  }
}
