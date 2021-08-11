package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeCreateTest02 extends AbstractIntegrationTest {

  test("create proposed orphan node") {

    withDatabase { database =>

      val dataBefore = OverpassData.empty
      val dataAfter = OverpassData().node(
        1001,
        version = 1,
        tags = Tags.from(
          "proposed:rwn_ref" -> "01",
          "network:type" ->
            "node_network"
        )
      )

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Create, dataAfter.rawNodeWithId(1001))

      assert(tc.analysisContext.data.nodes.watched.contains(1001))

      assertNode(tc)
      assertChangeSetSummary(tc)
      assertNodeChange(tc)
    }
  }

  private def assertNode(tc: IntegrationTestContext) = {
    tc.findNodeById(1001) should matchTo(
      newNodeDoc(
        1001,
        labels = Seq(
          "active",
          "network-type-hiking"
        ),
        country = Some(Country.nl),
        name = "01",
        names = Seq(
          newNodeName(
            NetworkType.hiking,
            NetworkScope.regional,
            "01",
            proposed = true
          )
        ),
        version = 1,
        tags = Tags.from(
          "proposed:rwn_ref" -> "01",
          "network:type" -> "node_network"
        )
      )
    )
  }

  private def assertChangeSetSummary(tc: IntegrationTestContext) = {
    tc.findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              added = Seq(newChangeSetElementRef(1001, "01", happy = true))
            )
          )
        ),
        subsetAnalyses = Seq(
          ChangeSetSubsetAnalysis(Subset.nlHiking, happy = true)
        ),
        happy = true
      )
    )
  }

  private def assertNodeChange(tc: IntegrationTestContext) = {
    tc.findNodeChangeById("123:1:1001") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Create,
        subsets = Seq(Subset.nlHiking),
        name = "01",
        after = Some(
          newMetaData(version = 1)
        ),
        happy = true,
        impact = true,
        locationHappy = true,
        locationImpact = true
      )
    )
  }
}
