package kpn.core.engine.changes.integration

import kpn.core.test.TestData
import kpn.core.test.TestData2
import kpn.shared.changes.ChangeAction
import kpn.shared.route.RouteInfo

class OrphanRouteTest06 extends AbstractTest {

  test("create ignored orphan route") {

    val dataBefore = TestData2().data

    val dataAfter = TestData2()
      .route(11, "01-02") // route without network nodes
      .data

    val tc = new TestConfig()

    tc.relationAfter(dataAfter, 11)
    tc.process(ChangeAction.Create, TestData.relation(dataAfter, 11))

    tc.analysisData.orphanRoutes.ignored.contains(11) should equal(true)

    (tc.analysisRepository.saveRoute _).verify(
      where { routeInfo: RouteInfo =>
        routeInfo.id should equal(11)
        routeInfo.active should equal(true)
        routeInfo.orphan should equal(true)
        routeInfo.ignored should equal(true)
        true
      }
    ).once()

    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never()
    (tc.changeSetRepository.saveRouteChange _).verify(*).never()
  }
}
