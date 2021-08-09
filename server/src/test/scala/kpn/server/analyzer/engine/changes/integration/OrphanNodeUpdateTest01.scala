package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.NodeName
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.mongo.doc.NodeDoc
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class OrphanNodeUpdateTest01 extends AbstractTest {

  test("update orphan node") {

    withDatabase { database =>

      val dataBefore = OverpassData().networkNode(
        1001,
        "01",
        extraTags = Tags.from(
          "tag" -> "before"
        )
      )

      val dataAfter = OverpassData().networkNode(
        1001,
        "01",
        extraTags = Tags.from("" +
          "tag" -> "after"
        )
      )

      val tc = new TestContext(database, dataBefore, dataAfter)

      tc.analysisContext.data.nodes.watched.add(1001)

      tc.process(ChangeAction.Modify, dataAfter.rawNodeWithId(1001))

      assert(tc.analysisContext.data.nodes.watched.contains(1001))

      tc.findNodeById(1001) should matchTo(
        NodeDoc(
          1001,
          labels = Seq(
            "active",
            "network-type-hiking"
          ),
          active = true,
          Some(Country.nl),
          "01",
          Seq(
            NodeName(
              NetworkType.hiking,
              NetworkScope.regional,
              "01",
              None,
              proposed = false
            )
          ),
          "0",
          "0",
          Timestamp(2015, 8, 11, 0, 0, 0),
          None,
          Tags.from(
            "rwn_ref" -> "01",
            "network:type" -> "node_network",
            "tag" -> "after"
          ),
          Seq.empty,
          Seq.empty,
          Seq.empty,
          None,
          Seq.empty
        )
      )

      tc.findChangeSetSummaryById("123:1") should matchTo(
        newChangeSetSummary(
          subsets = Seq(Subset.nlHiking),
          orphanNodeChanges = Seq(
            ChangeSetSubsetElementRefs(
              Subset.nlHiking,
              ChangeSetElementRefs(
                updated = Seq(
                  newChangeSetElementRef(1001, "01")
                )
              )
            )
          ),
          subsetAnalyses = Seq(
            ChangeSetSubsetAnalysis(Subset.nlHiking)
          )
        )
      )

      tc.findNodeChangeById("123:1:1001") should matchTo(
        newNodeChange(
          key = newChangeKey(elementId = 1001),
          changeType = ChangeType.Update,
          subsets = Seq(Subset.nlHiking),
          name = "01",
          before = Some(
            newRawNode(
              1001,
              tags = Tags.from(
                "rwn_ref" -> "01",
                "network:type" -> "node_network",
                "tag" -> "before"
              )
            )
          ),
          after = Some(
            newRawNode(
              1001,
              tags = Tags.from(
                "rwn_ref" -> "01",
                "network:type" -> "node_network",
                "tag" -> "after"
              )
            )
          ),
          tagDiffs = Some(
            TagDiffs(
              mainTags = Seq(
                TagDetail(TagDetailType.Same, "rwn_ref", Some("01"), Some("01")),
                TagDetail(TagDetailType.Same, "network:type", Some("node_network"), Some("node_network"))
              ),
              extraTags = Seq(
                TagDetail(TagDetailType.Update, "tag", Some("before"), Some("after"))
              )
            )
          ),
          facts = Seq(Fact.OrphanNode)
        )
      )
    }
  }
}
