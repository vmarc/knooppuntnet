package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSummary
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.NetworkChange
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.TestData2

class NetworkUpdateRouteTest05 extends AbstractTest {

  test("network update - an orphan route that is added to the network is no longer orphan") {

    pending

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route( // this orphan route is not referenced by the network
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
      .route( // the route definition itself has not changed
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
    tc.watchOrphanRoute(dataBefore, 11)
    tc.relationBefore(dataBefore, 11)
    tc.watchNetwork(dataBefore, 1)
    tc.relationBefore(dataBefore, 1)
    tc.relationAfter(dataAfter, 1)

    // before:
    assert(!tc.analysisContext.data.networks.watched.isReferencingRelation(11))
    assert(tc.analysisContext.data.routes.watched.contains(11))

    // act:
    tc.process(ChangeAction.Modify, relation(dataAfter, 1))

    // after:
    assert(tc.analysisContext.data.networks.watched.isReferencingRelation(11))
    assert(!tc.analysisContext.data.routes.watched.contains(11))

    (tc.networkRepository.oldSaveNetworkInfo _).verify(*).once()
    (tc.routeRepository.save _).verify(*).once()
    (tc.nodeRepository.save _).verify(*).never()

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
        networkChange should matchTo(
          newNetworkChange(
            newChangeKey(elementId = 1),
            ChangeType.Update,
            Some(Country.nl),
            NetworkType.hiking,
            1,
            "name",
            orphanRoutes = RefChanges(
              oldRefs = Seq(Ref(11, "01-02"))
            ),
            networkDataUpdate = Some(
              NetworkDataUpdate(
                newNetworkData(name = "name"),
                newNetworkData(name = "name")
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

        val routeData = newRouteData(
          Some(Country.nl),
          NetworkType.hiking,
          relation = newRawRelation(
            11,
            members = Seq(
              RawMember("way", 101, None)
            ),
            tags = newRouteTags("01-02")
          ),
          name = "01-02",
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
            addedToNetwork = Seq(
              Ref(1, "name")
            ),
            before = Some(routeData),
            after = Some(routeData),
            facts = Seq(Fact.WasOrphan),
            happy = true,
            impact = true,
            locationHappy = true,
            locationImpact = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveNodeChange _).verify(*).never()
  }
}
