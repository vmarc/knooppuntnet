package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeDeleteTest01 extends AbstractTest {

  test("delete orphan node") {

    withDatabase { database =>
      val dataBefore = OverpassData()
        .networkNode(1001, "01")

      val dataAfter = OverpassData.empty

      val tc = new TestContext(database, dataBefore, dataAfter)

      tc.analysisContext.data.nodes.watched.add(1001)

      tc.process(ChangeAction.Delete, newRawNode(1001))

      assert(!tc.analysisContext.data.nodes.watched.contains(1001))

      tc.findNodeById(1001) should matchTo(
        newNodeDoc(
          1001,
          labels = Seq(
            "facts",
            "fact-Deleted",
            "network-type-hiking"
          ),
          active = false, // <-- !!
          country = Some(Country.nl),
          name = "01",
          names = Seq(
            newNodeName(
              NetworkType.hiking,
              NetworkScope.regional,
              "01"
            )
          ),
          tags = newNodeTags("01"),
          facts = Seq(Fact.Deleted)
        )
      )

      tc.findChangeSetSummaryById("123:1") should matchTo(
        newChangeSetSummary(
          subsets = Seq(Subset.nlHiking),
          orphanNodeChanges = Seq(
            ChangeSetSubsetElementRefs(
              Subset.nlHiking,
              ChangeSetElementRefs(
                removed = Seq(
                  newChangeSetElementRef(1001, "01", investigate = true)
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

      tc.findNodeChangeById("123:1:1001") should matchTo(
        newNodeChange(
          key = newChangeKey(elementId = 1001),
          changeType = ChangeType.Delete,
          subsets = Seq(Subset.nlHiking),
          name = "01",
          before = Some(
            newRawNodeWithName(1001, "01")
          ),
          facts = Seq(Fact.WasOrphan, Fact.Deleted),
          investigate = true,
          impact = true,
          locationInvestigate = true,
          locationImpact = true
        )
      )
    }
  }

}
