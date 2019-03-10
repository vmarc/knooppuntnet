package kpn.core.engine.changes.integration

import kpn.core.changes.ElementIds
import kpn.core.test.TestData2
import kpn.shared.Fact
import kpn.shared.changes.ChangeAction
import kpn.shared.route.RouteInfo

class OrphanRouteTest08 extends AbstractTest {

  test("delete ignored orphan route") {

    val dataBefore = TestData2()
      .foreignNetworkNode(1001, "01")
      .foreignNetworkNode(1002, "02")
      .foreignNetworkNode(1003, "03")
      .foreignNetworkNode(1004, "04")
      .foreignNetworkNode(1005, "05")
      .foreignNetworkNode(1006, "06")
      .way(101, 1001, 1002, 1003, 1004, 1005, 1006)
      .route(
        11,
        "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      .data

    val dataAfter = TestData2().data

    val tc = new TestConfig()

    tc.analysisData.orphanRoutes.ignored.add(11, ElementIds())

    tc.relationBefore(dataBefore, 11)
    tc.process(ChangeAction.Delete, newRawRelation(11))

    tc.analysisData.orphanRoutes.ignored.contains(11) should equal(false)

    (tc.analysisRepository.saveRoute _).verify(
      where { routeInfo: RouteInfo =>
        routeInfo.id should equal(11)
        routeInfo.active should equal(false)
        routeInfo.orphan should equal(true)
        routeInfo.ignored should equal(true)
        routeInfo.facts should equal(Seq(Fact.IgnoreForeignCountry))
        true
      }
    ).once()

    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never()
    (tc.changeSetRepository.saveRouteChange _).verify(*).never()
  }
}
