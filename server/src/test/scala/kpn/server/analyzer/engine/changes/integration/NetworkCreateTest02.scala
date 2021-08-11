package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.common.data.raw.RawWay
import kpn.api.common.diff.RefDiffs
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.server.analyzer.engine.changes.changes.ElementIds

class NetworkCreateTest02 extends AbstractIntegrationTest {

  test("network create - orphan routes and orphan nodes are no longer orphan when part of the added network") {

    pending

    withDatabase { database =>

      val dataBefore = OverpassData()
        .networkNode(1001, "01")
        .networkNode(1002, "02")
        .way(101, 1001, 1002)
        .route(11, "01-02", Seq(newMember("way", 101)))

      val dataAfter = OverpassData()
        .networkNode(1001, "01")
        .networkNode(1002, "02")
        .way(101, 1001, 1002)
        .route(11, "01-02", Seq(newMember("way", 101)))
        .networkRelation(1, "name", Seq(newMember("relation", 11)))

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.analysisContext.data.routes.watched.add(11, ElementIds())
      tc.analysisContext.data.nodes.watched.add(1001L)
      tc.analysisContext.data.nodes.watched.add(1002L)

      assert(tc.analysisContext.data.routes.watched.contains(11))
      assert(tc.analysisContext.data.nodes.watched.contains(1001))
      assert(tc.analysisContext.data.nodes.watched.contains(1002))

      tc.process(ChangeAction.Create, dataAfter.rawRelationWithId(1))

      assert(!tc.analysisContext.data.routes.watched.contains(11))
      assert(!tc.analysisContext.data.nodes.watched.contains(1001))
      assert(!tc.analysisContext.data.nodes.watched.contains(1002))

      (tc.networkRepository.oldSaveNetworkInfo _).verify(
        where { networkInfo: NetworkInfo =>
          networkInfo.id should equal(1)
          // for remaining network structure - see NetworkAnalyzerTest
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

      tc.findNetworkChangeById("123:1:1") should matchTo(
        newNetworkChange(
          newChangeKey(elementId = 1),
          ChangeType.Create,
          Some(Country.nl),
          NetworkType.hiking,
          1,
          "name",
          orphanRoutes = RefChanges(oldRefs = Seq(Ref(11, "01-02"))),
          orphanNodes = RefChanges(oldRefs = Seq(Ref(1001, "01"), Ref(1002, "02"))),
          networkNodes = RefDiffs(added = Seq(Ref(1001, "01"), Ref(1002, "02"))),
          routes = RefDiffs(added = Seq(Ref(11, "01-02"))),
          happy = true
        )
      )

      tc.findRouteChangeById("123:1:11") should matchTo(
        newRouteChange(
          newChangeKey(elementId = 11),
          ChangeType.Update,
          "01-02",
          addedToNetwork = Seq(Ref(1, "name")),
          before = Some(
            newRouteData(
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
          ),
          after = Some(
            newRouteData(
              Some(Country.nl),
              NetworkType.hiking,
              relation = newRawRelation(11,
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
                RawWay(101, 0, Timestamp(2015, 8, 11, 0, 0, 0), 0, Seq(1001, 1002), Tags.from("highway" -> "unclassified"))
              )
            )
          ),
          facts = Seq(Fact.WasOrphan),
          happy = true,
          impact = true,
          locationHappy = true,
          locationImpact = true
        )
      )

      tc.findNodeChangeById("123:1:1001") should matchTo(
        newNodeChange(
          key = newChangeKey(elementId = 1001),
          changeType = ChangeType.Update,
          subsets = Seq(Subset.nlHiking),
          name = "01",
          before = Some(
            newMetaData()
          ),
          after = Some(
            newMetaData()
          ),
          addedToNetwork = Seq(
            Ref(1, "name")
          ),
          facts = Seq(Fact.WasOrphan),
          happy = true,
          impact = true
        )
      )

      tc.findNodeChangeById("123:1:1002") should matchTo(
        newNodeChange(
          key = newChangeKey(elementId = 1002),
          changeType = ChangeType.Update,
          subsets = Seq(Subset.nlHiking),
          name = "02",
          before = Some(
            newMetaData()
          ),
          after = Some(
            newMetaData()
          ),
          addedToNetwork = Seq(
            Ref(1, "name")
          ),
          facts = Seq(Fact.WasOrphan),
          happy = true,
          impact = true
        )
      )
    }
  }

}
