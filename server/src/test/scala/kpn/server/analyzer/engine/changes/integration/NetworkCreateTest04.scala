package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.NetworkExtraMemberWay
import kpn.api.common.NetworkFacts
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.diff.IdDiffs
import kpn.api.common.diff.RefDiffs
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class NetworkCreateTest04 extends AbstractTest {

  test("network create - investigate flag is set when issue in added network") {

    pending

    withDatabase { database =>

      val dataAfter = OverpassData()
        .networkNode(1001, "01")
        .networkNode(1002, "02")
        .way(101, 1001, 1002)
        .way(102)
        .route(11, "01-02", Seq(newMember("way", 101)))
        .networkRelation(
          1,
          "name",
          Seq(
            newMember("relation", 11),
            newMember("way", 102)
          )
        )

      val tc = new TestContext(database, OverpassData.empty, dataAfter)

      tc.process(ChangeAction.Create, dataAfter.rawRelationWithId(1))

      assert(tc.analysisContext.data.networks.watched.contains(1))

      (tc.networkRepository.oldSaveNetworkInfo _).verify(
        where { networkInfo: NetworkInfo =>
          networkInfo.id should equal(1)
          networkInfo.detail.get.networkFacts should matchTo(
            NetworkFacts(
              networkExtraMemberWay = Some(
                Seq(
                  NetworkExtraMemberWay(102)
                )
              )
            )
          )
          true
        }
      ).once()

      tc.findChangeSetSummaryById("123:1") should matchTo(
        newChangeSetSummary(
          subsets = Seq(Subset.nlHiking),
          networkChanges = NetworkChanges(
            creates = Seq(
              newChangeSetNetwork(
                Some(Country.nl),
                NetworkType.hiking,
                1,
                "name",
                routeChanges = ChangeSetElementRefs(
                  added = Seq(
                    ChangeSetElementRef(11, "01-02", happy = true, investigate = false)
                  )
                ),
                nodeChanges = ChangeSetElementRefs(
                  added = Seq(
                    ChangeSetElementRef(1001, "01", happy = true, investigate = false),
                    ChangeSetElementRef(1002, "02", happy = true, investigate = false)
                  )
                ),
                happy = true,
                investigate = true
              )
            )
          ),
          subsetAnalyses = Seq(
            ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true, investigate = true)
          ),
          happy = true,
          investigate = true
        )
      )

      tc.findNetworkChangeById("123:1:1") should matchTo(
        newNetworkChange(
          newChangeKey(elementId = 1),
          ChangeType.Create,
          Some(Country.nl),
          NetworkType.hiking,
          1,
          "name",
          networkNodes = RefDiffs(added = Seq(Ref(1001, "01"), Ref(1002, "02"))),
          routes = RefDiffs(added = Seq(Ref(11, "01-02"))),
          ways = IdDiffs(
            added = Seq(
              102
            )
          ),
          happy = true,
          investigate = true
        )
      )
    }
  }
}
