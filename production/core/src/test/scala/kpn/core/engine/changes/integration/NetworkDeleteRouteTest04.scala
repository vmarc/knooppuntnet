package kpn.core.engine.changes.integration

import kpn.core.changes.RelationAnalyzer
import kpn.core.test.TestData2
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkChanges
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.NetworkChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.changes.details.RouteChange
import kpn.shared.common.Ref
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawMember
import kpn.shared.network.NetworkInfo

class NetworkDeleteRouteTest04 extends AbstractTest {

  test("network delete - route becomes ignored") {

    val dataBefore = TestData2()
      .networkNode(1001, "01") // referenced in network1 and network2 and orphan route
      .networkNode(1002, "02") // referenced in network1
      .networkNode(1003, "03") // referenced in network2
      .networkNode(1004, "04") // referenced in orphan route
      .way(101, 1001, 1002) // route 11 only referenced in network 1
      .route(11, "01-02", Seq(newMember("way", 101)))
      .way(102, 1001, 1003) // route 12 referenced in network 1 and network 2
      .route(12, "01-03", Seq(newMember("way", 102)))
      .networkRelation(1, "network1", Seq(newMember("relation", 11), newMember("relation", 12)))
      .networkRelation(2, "network2", Seq(newMember("relation", 12)))
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkNode(1003, "03")
      .networkNode(1004, "04")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember("way", 101))) // route has become orphan
      .way(102, 1001, 1003) // route 12 still referenced in network 2
      .route(12, "01-03", Seq(newMember("way", 102)))
      .networkRelation(2, "network2", Seq(newMember("relation", 12)))
      .data

    val tc = new TestConfig()
    tc.relationBefore(dataBefore, 1)
    tc.relationAfter(dataAfter, 11)
    tc.relationAfter(dataAfter, 12)
    tc.nodesAfter(dataAfter, 1001, 1002, 1003)

    tc.analysisData.networks.watched.add(1, RelationAnalyzer.toElementIds(dataBefore.relations(1)))
    tc.analysisData.networks.watched.add(2, RelationAnalyzer.toElementIds(dataBefore.relations(2)))

    tc.process(ChangeAction.Delete, newRawRelation(1))

    // network 1 is no longer in memory
    tc.analysisData.networks.watched.contains(1) should equal(false)

    tc.analysisData.orphanRoutes.watched.contains(11) should equal(true) // network 1 was removed, route no longer referenced
    tc.analysisData.orphanRoutes.watched.contains(12) should equal(false) // network 1 was removed, but route still referenced in network 2

    tc.analysisData.orphanNodes.watched.contains(1001) should equal(false)
    tc.analysisData.orphanNodes.watched.contains(1002) should equal(false) // still referenced in orphan route
    tc.analysisData.orphanNodes.watched.contains(1003) should equal(false)

    (tc.networkRepository.save _).verify(
      where { (networkInfo: NetworkInfo) =>
        networkInfo should equal(
          newNetworkInfo(
            newNetworkAttributes(
              1,
              Some(Country.nl),
              NetworkType.hiking,
              "network1",
              lastUpdated = timestampAfterValue,
              relationLastUpdated = timestampAfterValue
            ),
            active = false // <--- !!!
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { (changeSetSummary: ChangeSetSummary) =>
        changeSetSummary should equal(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            networkChanges = NetworkChanges(
              deletes = Seq(
                newChangeSetNetwork(
                  Some(Country.nl),
                  NetworkType.hiking,
                  1,
                  "network1",
                  investigate = true
                )
              )
            ),
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { (networkChange: NetworkChange) =>
        networkChange should equal(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Delete,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "network1",
            orphanRoutes = RefChanges(newRefs = Seq(Ref(11, "01-02"))),
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveRouteChange _).verify(
      where { (routeChange: RouteChange) =>
        routeChange.id match {
          case 11 => assertRoute11(routeChange)
          case 12 => assertRoute12(routeChange)
          case _ => fail("unexpected routeId " + routeChange.id)
        }
        true
      }
    ).repeated(2)
  }

  private def assertRoute11(routeChange: RouteChange): Unit = {

    val routeData = newRouteData(
      Some(Country.nl),
      NetworkType.hiking,
      newRawRelation(
        11,
        members = Seq(
          RawMember("way", 101, None)
        ),
        tags = newRouteTags("01-02")
      ),
      "01-02",
      networkNodes = Seq(
        newRawNode(1001, tags = newNodeTags("01")),
        newRawNode(1002, tags = newNodeTags("02"))
      ),
      nodes = Seq(
        newRawNode(1001, tags = newNodeTags("01")),
        newRawNode(1002, tags = newNodeTags("02"))
      ),
      ways = Seq(
        newRawWay(
          101,
          nodeIds = Seq(1001, 1002),
          tags = Tags.from("highway" -> "unclassified")
        )
      )
    )

    routeChange should equal(
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        removedFromNetwork = Seq(Ref(1, "network1")),
        before = Some(routeData),
        after = Some(routeData),
        facts = Seq(Fact.BecomeOrphan)
      )
    )
  }

  private def assertRoute12(routeChange: RouteChange): Unit = {
    val routeData = newRouteData(
      Some(Country.nl),
      NetworkType.hiking,
      newRawRelation(
        12,
        members = Seq(
          RawMember("way", 102, None)
        ),
        tags = newRouteTags("01-03")
      ),
      "01-03",
      networkNodes = Seq(
        newRawNode(1001, tags = newNodeTags("01")),
        newRawNode(1003, tags = newNodeTags("03"))
      ),
      nodes = Seq(
        newRawNode(1001, tags = newNodeTags("01")),
        newRawNode(1003, tags = newNodeTags("03"))
      ),
      ways = Seq(
        newRawWay(
          102,
          nodeIds = Seq(1001, 1003),
          tags = Tags.from("highway" -> "unclassified")
        )
      )
    )

    routeChange should equal(
      newRouteChange(
        newChangeKey(elementId = 12),
        ChangeType.Update,
        "01-03",
        removedFromNetwork = Seq(Ref(1, "network1")),
        before = Some(routeData),
        after = Some(routeData)
      )
    )
  }
}
