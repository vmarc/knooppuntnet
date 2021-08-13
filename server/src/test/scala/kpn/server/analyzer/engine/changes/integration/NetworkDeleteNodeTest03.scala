package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.common.Ref
import kpn.api.common.network.NetworkInfo
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer

class NetworkDeleteNodeTest03 extends AbstractIntegrationTest {

  test("network delete - node still referenced in orphan route does not become orphan") {

    pending

    withDatabase { database =>

      val dataBefore = OverpassData()
        .networkNode(1001, "01")
        .route(11, "01-02", Seq(newMember("node", 1001)))
        .networkRelation(1, "network", Seq(newMember("node", 1001)))

      val dataAfter = OverpassData()
        .networkNode(1001, "01")
        .route(11, "01-02", Seq(newMember("node", 1001)))

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.analysisContext.data.networks.watched.add(1, RelationAnalyzer.toElementIds(tc.beforeRelationWithId(1)))
      tc.analysisContext.data.routes.watched.add(11, RelationAnalyzer.toElementIds(tc.beforeRelationWithId(11)))

      tc.process(ChangeAction.Delete, newRawRelation(1))

      assert(!tc.analysisContext.data.networks.watched.contains(1))
      assert(tc.analysisContext.data.routes.watched.contains(11))
      assert(!tc.analysisContext.data.nodes.watched.contains(1001))

      (tc.networkRepository.oldSaveNetworkInfo _).verify(
        where { networkInfo: NetworkInfo =>
          networkInfo should matchTo(
            newNetworkInfo(
              newNetworkAttributes(
                1,
                Some(Country.nl),
                NetworkType.hiking,
                name = "network",
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
                "network",
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

      tc.findNetworkInfoChangeById("123:1:1") should matchTo(
        newNetworkInfoChange(
          newChangeKey(elementId = 1),
          ChangeType.Delete,
          Some(Country.nl),
          NetworkType.hiking,
          1,
          "network",
          investigate = true
        )
      )

      assert(database.routeChanges.findAll().isEmpty)

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
          removedFromNetwork = Seq(
            Ref(1, "network")
          ),
          investigate = true,
          impact = true
        )
      )
    }
  }

}
