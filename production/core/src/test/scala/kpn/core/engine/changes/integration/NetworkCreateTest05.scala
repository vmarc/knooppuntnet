package kpn.core.engine.changes.integration

import kpn.core.test.TestData2
import kpn.shared.Fact
import kpn.shared.changes.ChangeAction
import kpn.shared.network.NetworkInfo

class NetworkCreateTest05 extends AbstractTest {

  test("network create - added to ignored list in memory and added to repository") {

    val data = TestData2()
      .foreignNetworkNode(1001, "01")
      .foreignNetworkNode(1002, "02")
      .foreignNetworkNode(1003, "03")
      .foreignNetworkNode(1004, "04")
      .foreignNetworkNode(1005, "05")
      .foreignNetworkNode(1006, "06")
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001),
          newMember("node", 1002),
          newMember("node", 1003),
          newMember("node", 1004),
          newMember("node", 1005),
          newMember("node", 1006)
        )
      )
      .data

    val tc = new TestConfig()
    tc.relationAfter(data, 1)

    tc.process(ChangeAction.Create, relation(data, 1))

    tc.analysisData.networks.ignored.contains(1) should equal(true)

    (tc.analysisRepository.saveIgnoredNetwork _).verify(
      where { (network: NetworkInfo) =>
        network.id should equal(1)
        network.attributes.name should equal("name")
        network.facts should equal(Seq(Fact.IgnoreForeignCountry))
        true
      }
    )

    (tc.analysisRepository.saveNetwork _).verify(*).never
    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never
    (tc.changeSetRepository.saveNetworkChange _).verify(*).never
  }
}
