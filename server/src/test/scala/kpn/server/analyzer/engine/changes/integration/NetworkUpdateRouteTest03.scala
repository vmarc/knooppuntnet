package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.NodeName
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.network.NetworkInfo
import kpn.api.common.route.RouteInfo
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.mongo.doc.NodeDoc
import kpn.core.test.TestData2

class NetworkUpdateRouteTest03 extends AbstractTest {

  test("network update - route no longer part of the network after deletion") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .networkNode(1003, "03")
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .route(12, "02-03", Seq(newMember("way", 102)))
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("relation", 11),
          newMember("relation", 12)
        )
      )
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02", Seq(newMember("way", 101)))
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("relation", 11)
        )
      )
      .data

    val tc = new TestConfig()
    tc.relationBefore(dataBefore, 1)
    tc.relationAfter(dataAfter, 1)
    tc.relationAfter(dataAfter, 12)
    tc.nodeAfter(dataAfter, 1003)
    tc.nodesAfter(dataAfter, 1003)
    tc.watchNetwork(dataBefore, 1)

    tc.process(ChangeAction.Delete, newRawRelation(12))

    tc.analysisContext.data.networks.watched.get(1) match {
      case None => fail()
      case Some(elementIds) =>

        elementIds.relationIds should contain(11)
        elementIds.relationIds should not contain 12

        elementIds.wayIds should contain(101)
        elementIds.wayIds should not contain 102

        elementIds.nodeIds should contain(1001)
        elementIds.nodeIds should contain(1002)
        elementIds.nodeIds should not contain 1003
    }

    (tc.networkRepository.save _).verify(
      where { networkInfo: NetworkInfo =>
        networkInfo.routeRefs should equal(Seq(11L))
        true
      }
    )

    (tc.routeRepository.save _).verify(
      where { routeInfo: RouteInfo =>
        if (routeInfo.id == 11) {
          routeInfo.active should equal(true)
        }
        if (routeInfo.id == 12) {
          routeInfo.active should equal(false)
        }
        true
      }
    ).repeat(2)

    (tc.nodeRepository.save _).verify(
      where { nodeDoc: NodeDoc =>
        nodeDoc.copy(tiles = Seq.empty) should matchTo(
          newNodeDoc(
            1003,
            labels = Seq(
              "facts",
              "fact-Deleted",
              "network-type-hiking"
            ),
            active = false,
            country = Some(Country.nl),
            name = "03",
            names = Seq(
              NodeName(
                NetworkType.hiking,
                NetworkScope.regional,
                "03", None,
                proposed = false
              )
            ),
            tags = newNodeTags("03"),
            facts = Seq(Fact.Deleted)
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should matchTo(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            networkChanges = NetworkChanges(
              updates = Seq(
                newChangeSetNetwork(
                  Some(Country.nl),
                  NetworkType.hiking,
                  1,
                  "name",
                  routeChanges = ChangeSetElementRefs(
                    removed = Seq(newChangeSetElementRef(12, "02-03", investigate = true))
                  ),
                  nodeChanges = ChangeSetElementRefs(
                    removed = Seq(newChangeSetElementRef(1003, "03", investigate = true)),
                    updated = Seq(newChangeSetElementRef(1002, "02"))
                  ),
                  investigate = true
                )
              )
            ),
            subsetAnalyses = Seq(
              ChangeSetSubsetAnalysis(Subset.nlHiking, investigate = true)
            ),
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNetworkChange _).verify(
      where { networkChange: NetworkChange =>
        networkChange should matchTo(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Update,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "name",
            networkDataUpdate = Some(
              NetworkDataUpdate(
                newNetworkData(name = "name"),
                newNetworkData(name = "name")
              )
            ),
            networkNodes = RefDiffs(
              removed = Seq(
                Ref(1003, "03")
              ),
              updated = Seq(
                Ref(1002, "02")
              )
            ),
            routes = RefDiffs(
              removed = Seq(
                Ref(12, "02-03")
              )
            ),
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveRouteChange _).verify(
      where { routeChange: RouteChange =>
        routeChange should matchTo(
          newRouteChange(
            newChangeKey(elementId = 12),
            ChangeType.Delete,
            "02-03",
            removedFromNetwork = Seq(Ref(1, "name")),
            before = Some(
              newRouteData(
                Some(Country.nl),
                NetworkType.hiking,
                relation = newRawRelation(
                  12,
                  members = Seq(RawMember("way", 102, None)),
                  tags = newRouteTags("02-03")
                ),
                name = "02-03",
                networkNodes = Seq(
                  newRawNodeWithName(1002, "02"),
                  newRawNodeWithName(1003, "03")
                ),
                nodes = Seq(
                  newRawNodeWithName(1002, "02"),
                  newRawNodeWithName(1003, "03")
                ),
                ways = Seq(
                  newRawWay(
                    102,
                    nodeIds = Seq(1002, 1003),
                    tags = Tags.from("highway" -> "unclassified")
                  )
                )
              )
            ),
            facts = Seq(Fact.Deleted),
            investigate = true,
            impact = true,
            locationInvestigate = true,
            locationImpact = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { nodeChange: NodeChange =>

        nodeChange.id match {

          case 1002 =>

            nodeChange should matchTo(
              newNodeChange(
                key = newChangeKey(elementId = 1002),
                changeType = ChangeType.Update,
                subsets = Seq(Subset.nlHiking),
                name = "02",
                before = Some(
                  newRawNodeWithName(1002, "02")
                ),
                after = Some(
                  newRawNodeWithName(1002, "02")
                ),
                removedFromRoute = Seq(Ref(12, "02-03")),
                investigate = true,
                impact = true,
                locationInvestigate = true,
                locationImpact = true
              )
            )
            true

          case 1003 =>

            nodeChange should matchTo(
              newNodeChange(
                key = newChangeKey(elementId = 1003),
                changeType = ChangeType.Delete,
                subsets = Seq(Subset.nlHiking),
                name = "03",
                before = Some(
                  newRawNodeWithName(1003, "03")
                ),
                after = None,
                removedFromRoute = Seq(Ref(12, "02-03")),
                removedFromNetwork = Seq(Ref(1, "name")),
                facts = Seq(Fact.Deleted),
                investigate = true,
                impact = true,
                locationInvestigate = true,
                locationImpact = true
              )
            )
            true

          case _ => false
        }
      }
    ).repeated(2)
  }
}
