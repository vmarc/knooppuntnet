package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.core.test.OverpassData

class RouteToNetworkTest01 extends IntegrationTest {

  test("change route to network") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .route(
        1,
        "01-02",
        members = Seq(RawMember(MemberType.Node, 1001, None)),
        version = 1
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkRelation(
        1,
        "01-02",
        members = Seq(RawMember(MemberType.Node, 1001, None)),
        version = 2
      )

    testIntegration(dataBefore, dataAfter) {

      processRelation(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      findOrphanRoutes().find(_.id == 1) should equal(None)

      val route = findRouteById(1)
      route.active should equal(false)
      route.summary.name should equal("01-02")
      route.version should equal(1)

      val baseNetwork = findBaseNetworkById(1)
      baseNetwork.active should equal(true)
      baseNetwork.version should equal(2)

      val network = findNetworkById(1)
      network.active should equal(true)
      network.summary.name should equal("01-02")

      val routeChange = findRouteChangeById("123:1:1")
      routeChange.changeType should equal(ChangeType.Update)
      routeChange.name should equal("01-02")

      val networkChange = findNetworkChangeById("123:1:1")
      networkChange.changeType should equal(ChangeType.Create)
      networkChange.networkName should equal("01-02")
    }
  }
}
