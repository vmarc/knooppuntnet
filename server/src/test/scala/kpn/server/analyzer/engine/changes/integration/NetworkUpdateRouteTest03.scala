package kpn.server.analyzer.engine.changes.integration

import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.TestData2
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.NodeInfo
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.NodeChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.common.route.RouteInfo

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

        elementIds.relationIds.contains(11) should equal(true)
        elementIds.relationIds.contains(12) should equal(false)

        elementIds.wayIds.contains(101) should equal(true)
        elementIds.wayIds.contains(102) should equal(false)

        elementIds.nodeIds.contains(1001) should equal(true)
        elementIds.nodeIds.contains(1002) should equal(true)
        elementIds.nodeIds.contains(1003) should equal(false)
    }

    (tc.analysisRepository.saveNetwork _).verify(*).once()

    (tc.analysisRepository.saveRoute _).verify(
      where { routeInfo: RouteInfo =>
        routeInfo should equal(
          newRouteInfo(
            newRouteSummary(
              12,
              Some(Country.nl),
              NetworkType.hiking,
              "02-03",
              wayCount = 1,
              nodeNames = Seq("02", "03"),
              tags = newRouteTags("02-03")
            ),
            active = false,
            tags = newRouteTags("02-03")
          )
        )
        true
      }
    )

    (tc.analysisRepository.saveNode _).verify(
      where { nodeInfo: NodeInfo =>
        nodeInfo should equal(
          newNodeInfo(
            1003,
            active = false,
            country = Some(Country.nl),
            tags = newNodeTags("03"),
            facts = Seq(Fact.Deleted)
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
        networkChange should equal(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Update,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "name",
            networkDataUpdate = Some(
              NetworkDataUpdate(
                NetworkData(
                  newRawRelation(
                    1,
                    members = Seq(
                      RawMember("relation", 11, None),
                      RawMember("relation", 12, None)
                    ),
                    tags = newNetworkTags()
                  ),
                  "name"
                ),
                NetworkData(
                  newRawRelation(
                    1,
                    members = Seq(
                      RawMember("relation", 11, None)
                    ),
                    tags = newNetworkTags()
                  ),
                  "name"
                )
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
        routeChange should equal(
          newRouteChange(
            newChangeKey(elementId = 12),
            ChangeType.Delete,
            "02-03",
            removedFromNetwork = Seq(Ref(1, "name")),
            before = Some(
              newRouteData(
                Some(Country.nl),
                NetworkType.hiking,
                newRawRelation(
                  12,
                  members = Seq(RawMember("way", 102, None)),
                  tags = newRouteTags("02-03")
                ),
                "02-03",
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
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(
      where { nodeChange: NodeChange =>

        nodeChange.id match {

          case 1002 =>

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
                removedFromRoute = Seq(Ref(12, "02-03")),
                investigate = true
              )
            )
            true

          case 1003 =>

            nodeChange should equal(
              newNodeChange(
                newChangeKey(elementId = 1003),
                ChangeType.Delete,
                Seq(Subset.nlHiking),
                "03",
                before = Some(
                  newRawNodeWithName(1003, "03")
                ),
                after = None,
                removedFromRoute = Seq(Ref(12, "02-03")),
                removedFromNetwork = Seq(Ref(1, "name")),
                facts = Seq(Fact.Deleted),
                investigate = true
              )
            )
            true

          case _ => false
        }
      }
    ).repeated(2)
  }
}
