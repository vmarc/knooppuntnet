package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRef
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeDeleteTest04 extends AbstractIntegrationTest {

  test("orphan node looses node tag") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01", version = 1)

    val dataAfter = OverpassData()
      .node(1001, version = 2) // rwn_ref tag no longer available, but node still exists

    withDatabase { database =>

      val tc = new IntegrationTestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Modify, dataAfter.rawNodeWithId(1001))

      assert(!tc.analysisContext.watched.nodes.contains(1001))

      assertNode(tc)
      assertNodeChange(tc)
      assertChangeSetSummary(tc)
    }
  }

  private def assertNode(tc: IntegrationTestContext): Unit = {
    tc.findNodeById(1001) should matchTo(
      newNodeDoc(
        1001,
        version = 2,
        labels = Seq.empty, // <-- !!
        active = false, // <-- !!
        country = Some(Country.nl),
      )
    )
  }

  private def assertChangeSetSummary(tc: IntegrationTestContext): Unit = {
    tc.findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        nodeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              removed = Seq(
                ChangeSetElementRef(1001, "01", happy = false, investigate = true)
              )
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

  private def assertNodeChange(tc: IntegrationTestContext): Unit = {
    tc.findNodeChangeById("123:1:1001") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Delete,
        subsets = Seq(Subset.nlHiking),
        name = "01",
        before = Some(
          newMetaData(version = 1)
        ),
        after = Some(
          newMetaData(version = 2)
        ),
        tagDiffs = Some(
          TagDiffs(
            Seq(
              TagDetail(TagDetailType.Delete, "rwn_ref", Some("01"), None),
              TagDetail(TagDetailType.Delete, "network:type", Some("node_network"), None)
            )
          )
        ),
        facts = Seq(Fact.LostHikingNodeTag),
        investigate = true,
        impact = true,
        locationInvestigate = true,
        locationImpact = true
      )
    )
  }
}
