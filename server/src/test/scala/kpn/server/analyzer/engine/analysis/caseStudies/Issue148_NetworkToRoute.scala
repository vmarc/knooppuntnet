package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.ChangeType
import kpn.api.custom.Tags
import kpn.core.doc.Label
import kpn.core.test.OverpassData
import kpn.server.analyzer.engine.changes.integration.IntegrationTest

class Issue148_NetworkToRoute extends IntegrationTest {

  test("change type=network to type=route") {
    val dataBefore = OverpassData()
      .node(
        1001,
        Tags.from(
          "rcn_ref" -> "01",
          "network:type" -> "node_network"
        )
      )
      .relation(
        11774118L,
        members = Seq(RawMember("node", 1001, None)),
        tags = Tags.from(
          "cycle_network" -> "rfn_gent",
          "network" -> "rcn",
          "network:type" -> "node_network",
          "note" -> "will be signposted soon (Stadsregionaal Fietsroutenetwerk Gent)",
          "ref" -> "72-84",
          "route" -> "bicycle",
          "state" -> "proposed",
          "type" -> "network"
        ),
        version = 1
      )

    val dataAfter = OverpassData()
      .node(
        1001,
        Tags.from(
          "rcn_ref" -> "01",
          "network:type" -> "node_network"
        )
      )
      .relation(
        11774118L,
        members = Seq(RawMember("node", 1001, None)),
        tags = Tags.from(
          "cycle_network" -> "rfn_gent",
          "network" -> "rcn",
          "network:type" -> "node_network",
          "note" -> "will be signposted soon (Stadsregionaal Fietsroutenetwerk Gent)",
          "ref" -> "72-84",
          "route" -> "bicycle",
          "state" -> "proposed",
          "type" -> "route"
        ),
        version = 2
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11774118L))

      val network = database.networks.findById(11774118L).get
      network.active should equal(false)
      network.version should equal(1)

      val networkInfo = database.networkInfos.findById(11774118L).get
      networkInfo.active should equal(false)
      networkInfo.summary.name should equal("no-name")

      val route = database.routes.findById(11774118L).get
      route.labels should contain(Label.active)
      route.summary.name should equal("72-84")
      route.version should equal(2)

      val orphanRoute = database.orphanRoutes.findById(11774118L).get
      orphanRoute.name should equal("72-84")

      val networkChange = database.networkChanges.findByStringId("123:1:11774118").get
      networkChange.changeType should equal(ChangeType.Delete)
      networkChange.networkName should equal("no-name")

      val networkInfoChange = database.networkChanges.findByStringId("123:1:11774118").get
      networkInfoChange.changeType should equal(ChangeType.Delete)
      networkInfoChange.networkName should equal("no-name")

      val routeChange = database.routeChanges.findByStringId("123:1:11774118").get
      routeChange.changeType should equal(ChangeType.Create)
      routeChange.name should equal("72-84")
    }
  }
}
