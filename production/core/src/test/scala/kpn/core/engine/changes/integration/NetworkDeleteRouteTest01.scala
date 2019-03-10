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
import kpn.shared.changes.details.NodeChange
import kpn.shared.changes.details.RefChanges
import kpn.shared.changes.details.RouteChange
import kpn.shared.common.Ref
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawMember
import kpn.shared.network.NetworkInfo

class NetworkDeleteRouteTest01 extends AbstractTest {

  test("network delete - route becomes orphan") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .networkRelation(1, "network", Seq(newMember("relation", 11)))
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .data

    val tc = new TestConfig()
    tc.relationBefore(dataBefore, 1)
    tc.relationAfter(dataAfter, 11)
    tc.nodesAfter(dataAfter, 1001, 1002)

    tc.analysisData.networks.watched.add(1, RelationAnalyzer.toElementIds(dataBefore.relations(1)))

    tc.process(ChangeAction.Delete, newRawRelation(1))

    tc.analysisData.networks.watched.contains(1) should equal(false)
    tc.analysisData.orphanRoutes.watched.contains(11) should equal(true)

    (tc.networkRepository.save _).verify(
      where { networkInfo: NetworkInfo =>
        networkInfo should equal(
          newNetworkInfo(
            newNetworkAttributes(
              1,
              Some(Country.nl),
              NetworkType.hiking,
              "network",
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
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            networkChanges = NetworkChanges(
              deletes = Seq(
                newChangeSetNetwork(
                  Some(Country.nl),
                  NetworkType.hiking,
                  1,
                  "network",
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
      where { networkChange: NetworkChange =>
        networkChange should equal(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Delete,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "network",
            orphanRoutes = RefChanges(newRefs = Seq(Ref(11, "01-02"))),
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveRouteChange _).verify(
      where { routeChange: RouteChange =>

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
            removedFromNetwork = Seq(Ref(1, "network")),
            before = Some(routeData),
            after = Some(routeData),
            facts = Seq(Fact.BecomeOrphan)
          )
        )

        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { nodeChange: NodeChange =>

        if (nodeChange.id == 1001) {
          nodeChange should equal(
            newNodeChange(
              newChangeKey(elementId = 1001),
              ChangeType.Update,
              Seq(Subset.nlHiking),
              "01",
              before = Some(
                newRawNodeWithName(1001, "01")
              ),
              after = Some(
                newRawNodeWithName(1001, "01")
              ),
              removedFromNetwork = Seq(
                Ref(1, "network")
              )
            )
          )
        }
        else if (nodeChange.id == 1002) {
          nodeChange should equal(
            newNodeChange(
              newChangeKey(elementId = 1002),
              ChangeType.Update,
              Seq(Subset.nlHiking),
              "02",
              before = Some(
                newRawNodeWithName(1002, "02")
              ),
              after = Some(
                newRawNodeWithName(1002, "02")
              ),
              removedFromNetwork = Seq(
                Ref(1, "network")
              )
            )
          )
        }
        else {
          fail(s"Unexpected node id ${nodeChange.id}")
        }

        true
      }
    ).repeated(2)
  }
}
