package kpn.server.analyzer.engine.analysis.network.main.analyzers

import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.api.custom.Change
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newMember
import kpn.server.analyzer.engine.changes.integration.IntegrationTest

class NetworkExtraAnalyzerIntegrationTest extends IntegrationTest {

  test("networkExtraMemberNode - not generated when proposed node in non-proposed network") {

    val dataBefore = OverpassData()

    val dataAfter = OverpassData()
      .node(1001, tags = Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"))
      .node(1002, tags = Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "02"))
      .node(1003, tags = Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "03"))
      .node(1004, tags = Tags.from("network:type" -> "node_network", "rwn_ref" -> "04", "state" -> "proposed"))
      .node(1005, tags = Tags.from("network:type" -> "node_network", "rwn_name" -> "05", "state" -> "proposed"))
      .relation(
        1,
        Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002),
          newMember(MemberType.Node, 1003),
          newMember(MemberType.Node, 1004),
          newMember(MemberType.Node, 1005),
        ),
        Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "network-name",
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(
        Seq(
          Change(
            ChangeAction.Create,
            Seq(
              dataAfter.rawNodeWithId(1001),
              dataAfter.rawNodeWithId(1002),
              dataAfter.rawNodeWithId(1003),
              dataAfter.rawNodeWithId(1004),
              dataAfter.rawNodeWithId(1005),
              dataAfter.rawRelationWithId(1),
            )
          )
        )
      )

      val networkDoc = findNetworkById(1)

      networkDoc.facts should equal(Seq.empty)

      assertEqual(
        networkDoc.nodes.map(node => node.name -> node.proposed),
        Seq(
          "01" -> false,
          "02" -> true,
          "03" -> true,
          "04" -> true,
          "05" -> true,
        )
      )

      watched.nodes.ids should contain(1001)
      watched.nodes.ids should contain(1002)
      watched.nodes.ids should contain(1003)
      watched.nodes.ids should contain(1004)
      watched.nodes.ids should contain(1005)
    }
  }

  test("networkExtraMemberNode - not generated when non-proposed node in proposed network") {

    val dataBefore = OverpassData()

    val dataAfter = OverpassData()
      .node(1001, tags = Tags.from("network:type" -> "node_network", "rwn_ref" -> "01"))
      .node(1002, tags = Tags.from("network:type" -> "node_network", "proposed:rwn_ref" -> "02"))
      .node(1003, tags = Tags.from("network:type" -> "node_network", "proposed:rwn_name" -> "03"))
      .node(1004, tags = Tags.from("network:type" -> "node_network", "rwn_ref" -> "04", "state" -> "proposed"))
      .node(1005, tags = Tags.from("network:type" -> "node_network", "rwn_name" -> "05", "state" -> "proposed"))
      .relation(
        1,
        Seq(
          newMember(MemberType.Node, 1001),
          newMember(MemberType.Node, 1002),
          newMember(MemberType.Node, 1003),
          newMember(MemberType.Node, 1004),
          newMember(MemberType.Node, 1005),
        ),
        Tags.from(
          "network:type" -> "node_network",
          "type" -> "network",
          "network" -> "rwn",
          "name" -> "network-name",
          "state" -> "proposed"
        )
      )

    testIntegration(dataBefore, dataAfter) {

      process(
        Seq(
          Change(
            ChangeAction.Create,
            Seq(
              dataAfter.rawNodeWithId(1001),
              dataAfter.rawNodeWithId(1002),
              dataAfter.rawNodeWithId(1003),
              dataAfter.rawNodeWithId(1004),
              dataAfter.rawNodeWithId(1005),
              dataAfter.rawRelationWithId(1),
            )
          )
        )
      )

      val networkDoc = findNetworkById(1)

      networkDoc.facts should equal(Seq.empty)

      assertEqual(
        networkDoc.nodes.map(node => node.name -> node.proposed),
        Seq(
          "01" -> false,
          "02" -> true,
          "03" -> true,
          "04" -> true,
          "05" -> true,
        )
      )

      watched.nodes.ids should contain(1001)
      watched.nodes.ids should contain(1002)
      watched.nodes.ids should contain(1003)
      watched.nodes.ids should contain(1004)
      watched.nodes.ids should contain(1005)
    }
  }
}
