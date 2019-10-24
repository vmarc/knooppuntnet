package kpn.core.db.views

import kpn.core.db.TestDocBuilder
import kpn.core.db.couch.Couch
import kpn.core.db.views.FactsPerNetworkView.Row
import kpn.core.test.TestSupport.withDatabase
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.data.Tags
import org.scalatest.FunSuite
import org.scalatest.Matchers

class FactsPerNetworkViewTest extends FunSuite with Matchers {

  test("rows") {

    withDatabase { database =>

      val networkId = 5L

      new TestDocBuilder(database) {
        val detail = Some(
          networkInfoDetail(
            nodes = Seq(
              newNetworkNodeInfo2(
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
                name = "01-02",
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

      val rows = database.old.query(AnalyzerDesign, FactsPerNetworkView, Couch.uiTimeout, stale = false)().map(FactsPerNetworkView.convert)

      rows should equal(
        Seq(
          Row("nl", "rwn", "NameMissing", "network-name", networkId, None, None),
          Row("nl", "rwn", "NetworkExtraMemberNode", "network-name", networkId, None, None),
          Row("nl", "rwn", "NodeMemberMissing", "network-name", networkId, Some("01"), Some(1001)),
          Row("nl", "rwn", "RouteBroken", "network-name", networkId, Some("01-02"), Some(10)),
          Row("nl", "rwn", "RouteNameMissing", "network-name", networkId, Some("01-02"), Some(10))
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

      val rows = database.old.query(AnalyzerDesign, FactsPerNetworkView, Couch.uiTimeout, stale = false)().map(FactsPerNetworkView.convert)

      rows should equal(
        Seq(
          Row("nl", "rwn", "RouteBroken", "OrphanRoutes", 0, Some("01-02"), Some(11))
        )
      )
    }
  }

  test("orphan node") {
    orphanNodeTest(NetworkType.bicycle)
    orphanNodeTest(NetworkType.hiking)
    orphanNodeTest(NetworkType.horseRiding)
    orphanNodeTest(NetworkType.motorboat)
    orphanNodeTest(NetworkType.canoe)
    orphanNodeTest(NetworkType.inlineSkates)
  }

  private def orphanNodeTest(networkType: NetworkType): Unit = {

    withDatabase { database =>

      new TestDocBuilder(database) {
        node(
          1001,
          Country.nl,
          tags = Tags.from(networkType.nodeTagKey -> "01", "network:type" -> "node_network"),
          orphan = true,
          facts = Seq(Fact.IntegrityCheck)
        )
      }

      val rows = database.old.query(AnalyzerDesign, FactsPerNetworkView, Couch.uiTimeout, stale = false)().map(FactsPerNetworkView.convert)

      rows should equal(
        Seq(
          Row("nl", networkType.name, "IntegrityCheck", "OrphanNodes", 0, Some("01"), Some(1001))
        )
      )
    }
  }
}
