package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.NetworkChanges
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RefChanges
import kpn.api.common.common.Ref
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase
import kpn.server.analyzer.engine.changes.changes.RelationAnalyzer

class NetworkDeleteNodeTest01 extends AbstractIntegrationTest {

  test("network delete - node becomes orphan") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkRelation(
        1,
        "network",
        Seq(
          newMember("node", 1001)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Delete, newRawRelation(1))

      assert(!tc.analysisContext.data.networks.watched.contains(1))
      assert(tc.analysisContext.data.nodes.watched.contains(1001))

      assert(database.routeChanges.findAll().isEmpty)

      assertNetwork(tc)
      assertNetworkInfo(tc)
      assertChangeSetSummary(tc)
      assertNetworkChange(tc)
      assertNodeChange(tc)

      // TODO database.orphanNodes.findByStringId()
    }
  }

  private def assertNetwork(tc: IntegrationTestContext): Unit = {
    tc.findNetworkById(1) should matchTo(
      newNetwork(
        1,
        active = false,
      )
    )
  }

  private def assertNetworkInfo(tc: IntegrationTestContext): Unit = {
    tc.findNetworkInfoById(1) should matchTo(
      newNetworkInfoDoc(
        1,
        active = false,
      )
    )
  }

  private def assertChangeSetSummary(tc: IntegrationTestContext): Unit = {
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
  }

  private def assertNetworkChange(tc: IntegrationTestContext): Unit = {
    tc.findNetworkChangeById("123:1:1") should matchTo(
      newNetworkChange(
        newChangeKey(elementId = 1),
        ChangeType.Delete,
        Some(Country.nl),
        NetworkType.hiking,
        1,
        "network",
        orphanNodes = RefChanges(
          newRefs = Seq(
            Ref(1001, "01")
          )
        ),
        investigate = true
      )
    )
  }

  private def assertNodeChange(tc: IntegrationTestContext): Unit = {
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
        facts = Seq(Fact.BecomeOrphan),
        investigate = true,
        impact = true
      )
    )
  }
}
