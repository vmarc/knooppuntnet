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
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeCreateTest02 extends AbstractTest {

  test("create proposed orphan node") {

    withDatabase { database =>

      val dataBefore = OverpassData.empty
      val dataAfter = OverpassData().node(
        1001,
        tags = Tags.from(
          "proposed:rwn_ref" -> "01",
          "network:type" ->
            "node_network"
        )
      )

      val tc = new TestContext(database, dataBefore, dataAfter)

      tc.process(ChangeAction.Create, dataAfter.rawNodeWithId(1001))

      assert(tc.analysisContext.data.nodes.watched.contains(1001))

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
          tags = Tags.from(
            "proposed:rwn_ref" -> "01",
            "network:type" -> "node_network"
          )
        )
      )

      tc.findChangeSetSummaryById("123:1") should matchTo(
        newChangeSetSummary(
          subsets = Seq(Subset.nlHiking),
          orphanNodeChanges = Seq(
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

      tc.findNodeChangeById("123:1:1001") should matchTo(
        newNodeChange(
          key = newChangeKey(elementId = 1001),
          changeType = ChangeType.Create,
          subsets = Seq(Subset.nlHiking),
          name = "01",
          after = Some(
            newRawNode(
              1001,
              tags = Tags.from(
                "proposed:rwn_ref" -> "01",
                "network:type" -> "node_network"
              )
            )
          ),
          happy = true,
          impact = true,
          locationHappy = true,
          locationImpact = true
        )
      )
    }
  }
}
