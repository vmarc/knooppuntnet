package kpn.core.database.views.analyzer

import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.database.views.analyzer.FactView.FactViewKey
import kpn.core.db.TestDocBuilder
import kpn.core.test.TestSupport.withDatabase
import kpn.core.util.UnitTest

class FactViewTest extends UnitTest {

  test("rows") {

    withDatabase { database =>

      val networkId = 5L

      new TestDocBuilder(database) {
        val detail = Some(
          networkInfoDetail(
            nodes = Seq(
              newNetworkInfoNode(
                1001L,
                "01",
                facts = Seq(
                  Fact.NodeMemberMissing
                )
              )
            ),
            routes = Seq(
              networkRouteInfo(
                10L,
                facts = Seq(
                  Fact.RouteBroken,
                  Fact.RouteNameMissing
                )
              )
            )
          )
        )
        network(
          networkId,
          Subset.nlHiking,
          "network-name",
          facts = Seq(
            Fact.NameMissing,
            Fact.NetworkExtraMemberNode
          ),
          detail = detail
        )
      }

      FactView.query(database, stale = false) should equal(
        Seq(
          FactViewKey("nl", "hiking", "NameMissing", "network-name", networkId),
          FactViewKey("nl", "hiking", "NetworkExtraMemberNode", "network-name", networkId),
          FactViewKey("nl", "hiking", "NodeMemberMissing", "network-name", networkId),
          FactViewKey("nl", "hiking", "RouteBroken", "network-name", networkId),
          FactViewKey("nl", "hiking", "RouteNameMissing", "network-name", networkId)
        )
      )
    }
  }

  test("orphan route") {

    withDatabase { database =>

      new TestDocBuilder(database) {
        route(
          11,
          Subset.nlHiking,
          orphan = true,
          facts = Seq(Fact.RouteBroken)
        )
      }

      FactView.query(database, stale = false) should equal(
        Seq(
          FactViewKey("nl", "hiking", "RouteBroken", "OrphanRoutes", 0)
        )
      )
    }
  }

  test("orphan node rcn") {
    orphanNodeTest(NetworkType.cycling)
    orphanNodeTest(NetworkType.hiking)
    orphanNodeTest(NetworkType.horseRiding)
    orphanNodeTest(NetworkType.motorboat)
    orphanNodeTest(NetworkType.canoe)
    orphanNodeTest(NetworkType.inlineSkating)
  }

  private def orphanNodeTest(networkType: NetworkType): Unit = {

    withDatabase { database =>

      new TestDocBuilder(database) {
        val scopedNetworkType = ScopedNetworkType(NetworkScope.regional, networkType)
        node(
          11,
          Country.nl,
          tags = Tags.from(scopedNetworkType.nodeTagKey -> "01"),
          orphan = true,
          facts = Seq(Fact.IntegrityCheck)
        )
      }

      FactView.query(database, stale = false) should equal(
        Seq(
          FactViewKey("nl", networkType.name, "IntegrityCheck", "OrphanNodes", 0)
        )
      )
    }
  }
}
