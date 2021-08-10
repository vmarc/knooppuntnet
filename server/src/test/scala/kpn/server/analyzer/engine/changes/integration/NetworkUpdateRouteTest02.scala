package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.NetworkDataUpdate
import kpn.api.common.diff.RefDiffs
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class NetworkUpdateRouteTest02 extends AbstractTest {

  test("network update - route that is no longer part of the network after update, does not become an orphan route if still referenced in another network") {

    pending

    withDatabase { database =>

      val dataBefore = OverpassData()
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

      val dataAfter = OverpassData()
        .networkNode(1001, "01")
        .networkNode(1002, "02")
        .way(101, 1001, 1002)
        .route( // route still exists
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

      val tc = new TestContext(database, dataBefore, dataAfter)
      tc.watchNetwork(tc.before, 1)
      tc.watchNetwork(tc.before, 2)

      // before:
      assert(tc.analysisContext.data.networks.watched.isReferencingRelation(11))
      assert(!tc.analysisContext.data.routes.watched.contains(11))

      // act:
      tc.process(ChangeAction.Modify, dataAfter.rawRelationWithId(1))

      // after:
      assert(tc.analysisContext.data.networks.watched.isReferencingRelation(11))
      assert(!tc.analysisContext.data.routes.watched.contains(11))

      (tc.networkRepository.oldSaveNetworkInfo _).verify(*).once()
      (tc.routeRepository.save _).verify(*).never()
      (tc.nodeRepository.save _).verify(*).never()

      tc.findChangeSetSummaryById("123:1") should matchTo(
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

      tc.findNetworkChangeById("123:1:1") should matchTo(
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

      val routeData = newRouteData(
        Some(Country.nl),
        NetworkType.hiking,
        relation = newRawRelation(
          11,
          members = Seq(RawMember("way", 101, None)),
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

      tc.findRouteChangeById("123:1:11") should matchTo(
        newRouteChange(
          newChangeKey(elementId = 11),
          ChangeType.Update,
          "01-02",
          removedFromNetwork = Seq(Ref(1, "name")),
          before = Some(routeData),
          after = Some(routeData),
          investigate = true,
          impact = true
        )
      )

      assert(database.nodeChanges.findAll().isEmpty)
    }
  }
}
