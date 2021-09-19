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
import kpn.api.custom.NetworkScope
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.mongo.doc.Label
import kpn.core.test.OverpassData

class OrphanNodeUpdateTest01 extends IntegrationTest {

  test("update orphan node") {

    val dataBefore = OverpassData().networkNode(
      1001,
      "01",
      version = 1,
      extraTags = Tags.from(
        "tag" -> "before"
      )
    )

    val dataAfter = OverpassData().networkNode(
      1001,
      "01",
      version = 2,
      extraTags = Tags.from("" +
        "tag" -> "after"
      )
    )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawNodeWithId(1001))

      assert(watched.nodes.contains(1001))

      assertNode()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertNode(): Unit = {
    findNodeById(1001) should matchTo(
      newNodeDoc(
        1001,
        labels = Seq(
          Label.active,
          "network-type-hiking"
        ),
        country = Some(Country.nl),
        name = "01",
        names = Seq(
          NodeName(
            NetworkType.hiking,
            NetworkScope.regional,
            "01",
            None,
            proposed = false
          )
        ),
        version = 2,
        lastUpdated = Timestamp(2015, 8, 11, 0, 0, 0),
        tags = Tags.from(
          "rwn_ref" -> "01",
          "network:type" -> "node_network",
          "tag" -> "after"
        )
      )
    )
  }

  private def assertNodeChange(): Unit = {
    findNodeChangeById("123:1:1001") should matchTo(
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Update,
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
            mainTags = Seq(
              TagDetail(TagDetailType.Same, "rwn_ref", Some("01"), Some("01")),
              TagDetail(TagDetailType.Same, "network:type", Some("node_network"), Some("node_network"))
            ),
            extraTags = Seq(
              TagDetail(TagDetailType.Update, "tag", Some("before"), Some("after"))
            )
          )
        )
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        nodeChanges = Seq(
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
  }
}
