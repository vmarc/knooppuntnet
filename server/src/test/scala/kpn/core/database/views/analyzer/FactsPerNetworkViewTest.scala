package kpn.core.database.views.analyzer

import kpn.api.common.NodeName
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
import kpn.core.test.TestSupport.withCouchDatabase
import kpn.core.util.UnitTest

class FactsPerNetworkViewTest extends UnitTest {

  test("query") {

    withCouchDatabase { database =>

      val networkId = 5L

      new TestDocBuilder(database) {
        private val detail = Some(
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
                ),
                proposed = false
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
      nameMissingRefs should matchTo(Seq(NetworkFactRefs(networkId, "network-name")))

      val networkExtraMemberNodeRefs = FactsPerNetworkView.query(database, Subset.nlHiking, Fact.NameMissing, stale = false)
      networkExtraMemberNodeRefs should matchTo(Seq(NetworkFactRefs(networkId, "network-name")))

      val nodeMemberMissingRefs = FactsPerNetworkView.query(database, Subset.nlHiking, Fact.NodeMemberMissing, stale = false)
      nodeMemberMissingRefs should matchTo(Seq(NetworkFactRefs(networkId, "network-name", Seq(Ref(1001, "01")))))

      val routeBrokenRefs = FactsPerNetworkView.query(database, Subset.nlHiking, Fact.RouteBroken, stale = false)
      routeBrokenRefs should matchTo(Seq(NetworkFactRefs(networkId, "network-name", Seq(Ref(10, "01-02")))))

      val routeNameMissingRefs = FactsPerNetworkView.query(database, Subset.nlHiking, Fact.RouteNameMissing, stale = false)
      routeNameMissingRefs should matchTo(Seq(NetworkFactRefs(networkId, "network-name", Seq(Ref(10, "01-02")))))
    }
  }

  test("orphan route") {

    withCouchDatabase { database =>

      new TestDocBuilder(database) {
        route(
          11,
          Subset.nlHiking,
          orphan = true,
          facts = Seq(Fact.RouteBroken)
        )
      }

      pending // no 'orphan' info in RouteInfo anymore
      val refs = FactsPerNetworkView.query(database, Subset.nlHiking, Fact.RouteBroken, stale = false)
      refs should matchTo(Seq(NetworkFactRefs(0, "OrphanRoutes", Seq(Ref(11, "01-02")))))
    }
  }

  test("orphan node") {

    pending

    orphanNodeTest(NetworkType.cycling)
    orphanNodeTest(NetworkType.hiking)
    orphanNodeTest(NetworkType.horseRiding)
    orphanNodeTest(NetworkType.motorboat)
    orphanNodeTest(NetworkType.canoe)
    orphanNodeTest(NetworkType.inlineSkating)
  }

  private def orphanNodeTest(networkType: NetworkType): Unit = {

    withCouchDatabase { database =>

      new TestDocBuilder(database) {
        private val scopedNetworkType = ScopedNetworkType(NetworkScope.regional, networkType)
        node(
          1001,
          Country.nl,
          names = Seq(
            NodeName(
              scopedNetworkType.networkType,
              scopedNetworkType.networkScope,
              "01",
              None,
              proposed = false
            ),
          ),
          tags = Tags.from(
            scopedNetworkType.nodeRefTagKey -> "01",
            "network:type" -> "node_network"
          ),
          // orphan = true,
          facts = Seq(Fact.IntegrityCheck)
        )
      }

      val refs = FactsPerNetworkView.query(database, Subset.of(Country.nl, networkType).get, Fact.IntegrityCheck, stale = false)
      refs should matchTo(Seq(NetworkFactRefs(0, "OrphanNodes", Seq(Ref(1001, "01")))))
    }
  }
}
