package kpn.core.engine.changes.integration

import kpn.core.test.TestData
import kpn.core.test.TestData2
import kpn.shared.changes.ChangeAction
import kpn.shared.route.RouteInfo

class OrphanRouteTest07 extends AbstractTest {

  test("update ignored orphan route") {

    val dataBefore = TestData2()
      .route(11, "name1") // route without network nodes
      .data

    val dataAfter = TestData2()
      .route(11, "name2") // route without network nodes
      .data

    val tc = new TestConfig()

    tc.relationBefore(dataBefore, 11)
    tc.relationAfter(dataAfter, 11)
    tc.process(ChangeAction.Create, TestData.relation(dataAfter, 11))

    (tc.analysisRepository.saveRoute _).verify(
      where { routeInfo: RouteInfo =>
        routeInfo.id should equal(11)
        routeInfo.active should equal(true)
        routeInfo.orphan should equal(true)
        routeInfo.summary.name should equal("name2")
        true
      }
    ).once()

    (tc.changeSetRepository.saveChangeSetSummary _).verify(*).never()
    (tc.changeSetRepository.saveRouteChange _).verify(*).never()  }
}
