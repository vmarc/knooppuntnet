package kpn.server.analyzer.engine.changes.integration

import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.TestData2
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NetworkData
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs

class NetworkUpdateRouteTest02 extends AbstractTest {

  test("network update - route that is no longer part of the network after update, does not become an orphan route if still referenced in another network") {

    val dataBefore = TestData2()
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
          newMember("relation", 11)
        )
      )
      .networkRelation(
        2,
        "name",
        Seq(
          newMember("relation", 11)
        )
      )
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(// route still exists
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
          // route member is no longer included here
        )
      )
      .networkRelation(
        2,
        "name",
        Seq(
          newMember("relation", 11)
        )
      )
      .data

    val tc = new TestConfig()
    tc.relationBefore(dataBefore, 1)
    tc.watchNetwork(dataBefore, 1)
    tc.watchNetwork(dataBefore, 2)
    tc.relationAfter(dataAfter, 1)
    tc.relationAfter(dataAfter, 11)

    // before:
    assert(tc.analysisContext.data.networks.watched.isReferencingRelation(11))
    assert(!tc.analysisContext.data.orphanRoutes.watched.contains(11))

    // act:
    tc.process(ChangeAction.Modify, relation(dataAfter, 1))

    // after:
    assert(tc.analysisContext.data.networks.watched.isReferencingRelation(11))
    assert(!tc.analysisContext.data.orphanRoutes.watched.contains(11))

    (tc.analysisRepository.saveNetwork _).verify(*).once()
    (tc.analysisRepository.saveRoute _).verify(*).never()
    (tc.analysisRepository.saveNode _).verify(*).never()

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
                    removed = Seq(
                      newChangeSetElementRef(11, "01-02", investigate = true)
                    )
                  ),
                  nodeChanges = ChangeSetElementRefs(
                    updated = Seq(
                      newChangeSetElementRef(1001, "01"),
                      newChangeSetElementRef(1002, "02")
                    )
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
                ),
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
              removed = Seq(
                Ref(11, "01-02")
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

        val routeData = newRouteData(
          Some(Country.nl),
          NetworkType.hiking,
          newRawRelation(
            11,
            members = Seq(RawMember("way", 101, None)),
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

        routeChange should matchTo(
          newRouteChange(
            newChangeKey(elementId = 11),
            ChangeType.Update,
            "01-02",
            removedFromNetwork = Seq(Ref(1, "name")),
            before = Some(routeData),
            after = Some(routeData),
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(*).never()
  }
}
