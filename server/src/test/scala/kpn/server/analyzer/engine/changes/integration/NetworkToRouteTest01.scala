package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.core.test.OverpassData

class NetworkToRouteTest01 extends IntegrationTest {

  test("change network to route") {
    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkRelation(
        1,
        "01-02",
        members = Seq(RawMember(MemberType.Node, 1001, None)),
        version = 1
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .route(
        1,
        "01-02",
        members = Seq(RawMember(MemberType.Node, 1001, None)),
        version = 2
      )

    testIntegration(dataBefore, dataAfter, keepDatabaseAfterTest = true) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      val baseNetwork = findBaseNetworkById(1)
      baseNetwork.active should equal(false)
      baseNetwork.version should equal(1)

      val network = findNetworkById(1)
      network.active should equal(false)
      network.summary.name should equal("01-02")

      val route = findRouteById(1)
      route.active should equal(true)
      route.summary.name should equal("01-02")
      route.version should equal(2)

      val orphanRouteInfo = findOrphanRouteById(1)
      orphanRouteInfo.name should equal("01-02")

      val networkChange = findNetworkChangeById("123:1:1")
      networkChange.changeType should equal(ChangeType.Delete)
      networkChange.networkName should equal("01-02")

      val routeChange = findRouteChangeById("123:1:1")
      routeChange.changeType should equal(ChangeType.Create)
      routeChange.name should equal("01-02")
    }
  }
}
