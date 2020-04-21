package kpn.core.database.views.analyzer

import kpn.api.common.common.Ref
import kpn.api.common.subset.NetworkFactRefs
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.ScopedNetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.db.TestDocBuilder
import kpn.core.test.TestSupport.withDatabase
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class FactsPerNetworkViewTest extends FunSuite with Matchers {

  test("query") {

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

      val nameMissingRefs = FactsPerNetworkView.query(database, Subset.nlHiking, Fact.NameMissing, stale = false)
      nameMissingRefs should equal(Seq(NetworkFactRefs(networkId, "network-name")))

      val networkExtraMemberNodeRefs = FactsPerNetworkView.query(database, Subset.nlHiking, Fact.NameMissing, stale = false)
      networkExtraMemberNodeRefs should equal(Seq(NetworkFactRefs(networkId, "network-name")))

      val nodeMemberMissingRefs = FactsPerNetworkView.query(database, Subset.nlHiking, Fact.NodeMemberMissing, stale = false)
      nodeMemberMissingRefs should equal(Seq(NetworkFactRefs(networkId, "network-name", Seq(Ref(1001, "01")))))

      val routeBrokenRefs = FactsPerNetworkView.query(database, Subset.nlHiking, Fact.RouteBroken, stale = false)
      routeBrokenRefs should equal(Seq(NetworkFactRefs(networkId, "network-name", Seq(Ref(10, "01-02")))))

      val routeNameMissingRefs = FactsPerNetworkView.query(database, Subset.nlHiking, Fact.RouteNameMissing, stale = false)
      routeNameMissingRefs should equal(Seq(NetworkFactRefs(networkId, "network-name", Seq(Ref(10, "01-02")))))
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

      val refs = FactsPerNetworkView.query(database, Subset.nlHiking, Fact.RouteBroken, stale = false)
      refs should equal(Seq(NetworkFactRefs(0, "OrphanRoutes", Seq(Ref(11, "01-02")))))
    }
  }

  test("orphan node") {
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
          1001,
          Country.nl,
          tags = Tags.from(scopedNetworkType.nodeTagKey -> "01", "network:type" -> "node_network"),
          orphan = true,
          facts = Seq(Fact.IntegrityCheck)
        )
      }

      val refs = FactsPerNetworkView.query(database, Subset.of(Country.nl, networkType).get, Fact.IntegrityCheck, stale = false)
      refs should equal(Seq(NetworkFactRefs(0, "OrphanNodes", Seq(Ref(1001, "01")))))
    }
  }
}
