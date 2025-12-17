package kpn.server.analyzer.engine.analysis.caseStudies

import kpn.api.common.ChangeType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.api.common.data.raw.RawMember
import kpn.api.custom.Tags
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
        members = Seq(RawMember(MemberType.Node, 1001, None)),
        tags = Tags.from(
          "cycle_network" -> "rfn_gent",
          "network" -> "rcn",
          "network:type" -> "node_network",
          "note" -> "will be signposted soon (Stadsregionaal Fietsroutenetwerk Gent)",
          "ref" -> "72-84",
          "route" -> "bicycle",
          "state" -> "proposed",
          "type" -> "network" // <---
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
        members = Seq(RawMember(MemberType.Node, 1001, None)),
        tags = Tags.from(
          "cycle_network" -> "rfn_gent",
          "network" -> "rcn",
          "network:type" -> "node_network",
          "note" -> "will be signposted soon (Stadsregionaal Fietsroutenetwerk Gent)",
          "ref" -> "72-84",
          "route" -> "bicycle",
          "state" -> "proposed",
          "type" -> "route" // <---
        ),
        version = 2
      )

    testIntegration(dataBefore, dataAfter) {

      processRelation(ChangeAction.Modify, dataAfter.rawRelationWithId(11774118L))

      val baseNetwork = findBaseNetworkById(11774118L)
      baseNetwork.active should equal(false)
      baseNetwork.version should equal(1)

      val network = findNetworkById(11774118L)
      network.active should equal(false)
      network.summary.name should equal("no-name")

      val route = findRouteById(11774118L)
      route.active should equal(true)
      route.base.summary.name should equal("72-84")
      route.base.raw.version should equal(2)

      val orphanRouteInfo = findOrphanRouteById(11774118L)
      orphanRouteInfo.name should equal("72-84")

      val networkChange = findNetworkChangeById("123:1:11774118")
      networkChange.changeType should equal(ChangeType.Delete)
      networkChange.networkName should equal("no-name")

      val routeChange = findRouteChangeById("123:1:11774118")
      routeChange.changeType should equal(ChangeType.Create)
      routeChange.name should equal("72-84")
    }
  }
}
