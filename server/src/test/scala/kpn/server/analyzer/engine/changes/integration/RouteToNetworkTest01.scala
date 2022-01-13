package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.ChangeType
import kpn.core.doc.Label
import kpn.core.test.OverpassData

class RouteToNetworkTest01 extends IntegrationTest {

  test("change route to network") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .route(
        1,
        "01-02",
        members = Seq(RawMember("node", 1001, None)),
        version = 1
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkRelation(
        1,
        "01-02",
        members = Seq(RawMember("node", 1001, None)),
        version = 2
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      database.orphanRoutes.findById(1) should equal(None)

      val route = database.routes.findById(1).get
      route.labels should not contain Label.active
      route.summary.name should equal("01-02")
      route.version should equal(1)

      val network = database.networks.findById(1).get
      network.active should equal(true)
      network.version should equal(2)

      val networkInfo = database.networkInfos.findById(1).get
      networkInfo.active should equal(true)
      networkInfo.summary.name should equal("01-02")

      val routeChange = database.routeChanges.findByStringId("123:1:1").get
      routeChange.changeType should equal(ChangeType.Delete)
      routeChange.name should equal("01-02")

      val networkChange = database.networkChanges.findByStringId("123:1:1").get
      networkChange.changeType should equal(ChangeType.Create)
      networkChange.networkName should equal("01-02")

      val networkInfoChange = database.networkChanges.findByStringId("123:1:1").get
      networkInfoChange.changeType should equal(ChangeType.Create)
      networkInfoChange.networkName should equal("01-02")
    }
  }
}
