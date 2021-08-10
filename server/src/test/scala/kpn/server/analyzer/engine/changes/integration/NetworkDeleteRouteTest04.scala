package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.common.Ref
import kpn.api.common.data.raw.RawMember
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer

class NetworkDeleteRouteTest04 extends AbstractTest {

  test("network delete - route becomes ignored") {

    pending

    withDatabase { database =>

      val dataBefore = OverpassData()
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

      val dataAfter = OverpassData()
        .networkNode(1001, "01")
        .networkNode(1002, "02")
        .networkNode(1003, "03")
        .networkNode(1004, "04")
        .way(101, 1001, 1002)
        .route(11, "01-02", Seq(newMember("way", 101))) // route has become orphan
        .way(102, 1001, 1003) // route 12 still referenced in network 2
        .route(12, "01-03", Seq(newMember("way", 102)))
        .networkRelation(2, "network2", Seq(newMember("relation", 12)))

      val tc = new TestContext(database, dataBefore, dataAfter)

      tc.analysisContext.data.networks.watched.add(1, RelationAnalyzer.toElementIds(tc.beforeRelationWithId(1)))
      tc.analysisContext.data.networks.watched.add(2, RelationAnalyzer.toElementIds(tc.beforeRelationWithId(2)))

      tc.process(ChangeAction.Delete, newRawRelation(1))

      // network 1 is no longer in memory
      assert(!tc.analysisContext.data.networks.watched.contains(1))

      assert(tc.analysisContext.data.routes.watched.contains(11)) // network 1 was removed, route no longer referenced
      assert(!tc.analysisContext.data.routes.watched.contains(12)) // network 1 was removed, but route still referenced in network 2

      assert(!tc.analysisContext.data.nodes.watched.contains(1001))
      assert(!tc.analysisContext.data.nodes.watched.contains(1002)) // still referenced in orphan route
      assert(!tc.analysisContext.data.nodes.watched.contains(1003))

      (tc.networkRepository.oldSaveNetworkInfo _).verify(
        where { networkInfo: NetworkInfo =>
          networkInfo should matchTo(
            newNetworkInfo(
              newNetworkAttributes(
                1,
                Some(Country.nl),
                NetworkType.hiking,
                name = "network1",
                lastUpdated = timestampAfterValue,
                relationLastUpdated = timestampAfterValue
              ),
              active = false // <--- !!!
            )
          )
          true
        }
      )

      tc.findChangeSetSummaryById("123:1") should matchTo(
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
          subsetAnalyses = Seq(
            ChangeSetSubsetAnalysis(Subset.nlHiking, investigate = true)
          ),
          investigate = true
        )
      )

      tc.findNetworkChangeById("123:1:1") should matchTo(
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

      val routeData11 = newRouteData(
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

      tc.findRouteChangeById("123:1:11") should matchTo(
        newRouteChange(
          newChangeKey(elementId = 11),
          ChangeType.Update,
          "01-02",
          removedFromNetwork = Seq(Ref(1, "network1")),
          before = Some(routeData11),
          after = Some(routeData11),
          facts = Seq(Fact.BecomeOrphan),
          investigate = true,
          impact = true
        )
      )

      val routeData12 = newRouteData(
        Some(Country.nl),
        NetworkType.hiking,
        relation = newRawRelation(
          12,
          members = Seq(
            RawMember("way", 102, None)
          ),
          tags = newRouteTags("01-03")
        ),
        name = "01-03",
        networkNodes = Seq(
          newRawNodeWithName(1001, "01"),
          newRawNodeWithName(1003, "03")
        ),
        nodes = Seq(
          newRawNodeWithName(1001, "01"),
          newRawNodeWithName(1003, "03")
        ),
        ways = Seq(
          newRawWay(
            102,
            nodeIds = Seq(1001, 1003),
            tags = Tags.from("highway" -> "unclassified")
          )
        )
      )

      tc.findRouteChangeById("123:1:12") should matchTo(
        newRouteChange(
          newChangeKey(elementId = 12),
          ChangeType.Update,
          "01-03",
          removedFromNetwork = Seq(Ref(1, "network1")),
          before = Some(routeData12),
          after = Some(routeData12),
          investigate = true,
          impact = true
        )
      )
    }
  }
}
