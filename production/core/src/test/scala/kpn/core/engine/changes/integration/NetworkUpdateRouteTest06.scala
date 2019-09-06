package kpn.core.engine.changes.integration

import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSubsetAnalysis
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
import kpn.shared.diff.NetworkData
import kpn.shared.diff.NetworkDataUpdate
import kpn.shared.diff.RefDiffs

class NetworkUpdateRouteTest06 extends AbstractTest {

  test("network update - an ignored route that is added to the network is no longer ignored") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route( // this ignored route is not referenced by the network
        11,
        "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001),
          newMember("node", 1002)
          // the network does not reference the route
        )
      )
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(
        11,
        "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      .networkRelation(
        1,
        "name",
        Seq(
          newMember("node", 1001),
          newMember("node", 1002),
          newMember("relation", 11) // route is now part of the network
        )
      )
      .data

    val tc = new TestConfig()
    tc.ignoreOrphanRoute(dataBefore, 11)
    tc.relationBefore(dataBefore, 11)
    tc.watchNetwork(dataBefore, 1)
    tc.relationBefore(dataBefore, 1)
    tc.relationAfter(dataAfter, 1)

    // before:
    tc.analysisData.networks.watched.isReferencingRelation(11) should equal(false)
    tc.analysisData.orphanRoutes.watched.contains(11) should equal(false)
    tc.analysisData.orphanRoutes.ignored.contains(11) should equal(true)

    // act:
    tc.process(ChangeAction.Modify, relation(dataAfter, 1))

    // after:
    tc.analysisData.networks.watched.isReferencingRelation(11) should equal(true)
    tc.analysisData.orphanRoutes.watched.contains(11) should equal(false)
    tc.analysisData.orphanRoutes.ignored.contains(11) should equal(false)

    (tc.analysisRepository.saveNetwork _).verify(*).once()
    (tc.analysisRepository.saveRoute _).verify(*).never() // route saved via saveNetwork
    (tc.analysisRepository.saveNode _).verify(*).never() // nodes saved via saveNetwork

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
                    added = Seq(newChangeSetElementRef(11, "01-02", happy = true))
                  ),
                  nodeChanges = ChangeSetElementRefs(
                    updated = Seq(
                      newChangeSetElementRef(1001, "01"),
                      newChangeSetElementRef(1002, "02")
                    )
                  ),
                  happy = true
                )
              )
            ),
            subsetAnalyses = Seq(
              ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true)
            ),
            happy = true
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
            ignoredRoutes = RefChanges(
              oldRefs = Seq(Ref(11, "01-02"))
            ),
            networkDataUpdate = Some(
              NetworkDataUpdate(
                NetworkData(
                  newRawRelation(
                    1,
                    members = Seq(
                      RawMember("node", 1001, None),
                      RawMember("node", 1002, None)
                    ),
                    tags = newNetworkTags()
                  ),
                  "name"
                ),
                NetworkData(
                  newRawRelation(
                    1,
                    members = Seq(
                      RawMember("node", 1001, None),
                      RawMember("node", 1002, None),
                      RawMember("relation", 11, None)
                    ),
                    tags = newNetworkTags()
                  ),
                  "name"
                )
              )
            ),
            networkNodes = RefDiffs(
              updated = Seq(
                Ref(1001, "01"),
                Ref(1002, "02")
              )
            ),
            routes = RefDiffs(
              added = Seq(
                Ref(11, "01-02")
              )
            ),
            happy = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveRouteChange _).verify(
      where { routeChange: RouteChange =>
        routeChange should equal(
          newRouteChange(
            newChangeKey(elementId = 11),
            ChangeType.Update,
            "01-02",
            addedToNetwork = Seq(
              Ref(1, "name")
            ),
            before = Some(
              newRouteData(
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
                  newRawNodeWithName(1001, "01"),
                  newRawNodeWithName(1002, "02")
                ),
                nodes = Seq(
                  newRawNodeWithName(1001, "01"),
                  newRawNodeWithName(1002, "02")
                ),
                ways = Seq(
                  newRawWay(
                    101,
                    nodeIds = Seq(1001, 1002),
                    tags = Tags.from("highway" -> "unclassified")
                  )
                )
              )
            ),
            after = Some(
              newRouteData(
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
                  newRawNodeWithName(1001, "01"),
                  newRawNodeWithName(1002, "02")
                ),
                nodes = Seq(
                  newRawNodeWithName(1001, "01"),
                  newRawNodeWithName(1002, "02")
                ),
                ways = Seq(
                  newRawWay(
                    101,
                    nodeIds = Seq(1001, 1002),
                    tags = Tags.from("highway" -> "unclassified")
                  )
                )
              )
            ),
            facts = Seq(Fact.WasIgnored),
            happy = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(*).never()
  }
}
