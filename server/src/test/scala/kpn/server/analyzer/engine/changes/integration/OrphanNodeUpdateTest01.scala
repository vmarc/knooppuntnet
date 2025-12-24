package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeType
import kpn.api.common.Country
import kpn.api.common.NodeName
import kpn.api.common.RouteScope
import kpn.api.common.RouteType
import kpn.api.common.changes.ChangeAction
import kpn.api.common.diff.TagDiff
import kpn.api.common.diff.TagDiffs
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.api.custom.Timestamp
import kpn.core.doc.Label
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newChangeKey
import kpn.core.test.TestObjects.newChangeSetElementRef
import kpn.core.test.TestObjects.newChangeSetSummary
import kpn.core.test.TestObjects.newLocationChanges
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newNodeBaseData
import kpn.core.test.TestObjects.newNodeChange
import kpn.core.test.TestObjects.newNodeDoc
import kpn.core.test.TestObjects.newRaw

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
      extraTags = Tags.from("tag" -> "after"
      )
    )

    testIntegration(dataBefore, dataAfter) {

      processNode(ChangeAction.Modify, dataAfter.rawNodeWithId(1001))

      assert(watched.nodes.contains(1001))

      assertNode()
      assertNodeChange()
      assertChangeSetSummary()
    }
  }

  private def assertNode(): Unit = {
    assertEqual(
      findNodeById(1001),
      newNodeDoc(
        1001,
        base = newNodeBaseData(
          raw = newRaw(
            version = 2,
            tags = Tags.from(
              "rwn_ref" -> "01",
              "network:type" -> "node_network",
              "tag" -> "after"
            )
          ),
          name = Some("01"),
          names = Seq(
            NodeName(
              RouteType.hiking,
              RouteScope.regional,
              "01",
              None,
              proposed = false
            )
          ),
          lastUpdated = Timestamp(2015, 8, 11, 0, 0, 0),
          country = Some(Country.nl),
          locations = Seq("nl")
        ),
        labels = Seq(
          Label.routeType(RouteType.hiking),
          Label.location("nl")
        )
      )
    )
  }

  private def assertNodeChange(): Unit = {
    assertEqual(
      findNodeChangeById("1:1:1001"),
      newNodeChange(
        key = newChangeKey(elementId = 1001),
        changeType = ChangeType.Update,
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
        name = Some("01"),
        before = Some(
          newMetaData(version = 1)
        ),
        after = Some(
          newMetaData(version = 2)
        ),
        tagDiffs = Some(
          TagDiffs(
            mainTags = Seq(
              TagDiff.same("rwn_ref", "01"),
              TagDiff.same("network:type", "node_network")
            ),
            extraTags = Seq(
              TagDiff.update("tag", "before", "after")
            )
          )
        )
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    assertEqual(
      findChangeSetSummaryById("1:1"),
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        locations = Seq("nl"),
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
        ),
        locationChanges = Seq(
          newLocationChanges(
            routeType = RouteType.hiking,
            locationNames = Seq("nl"),
            nodeChanges = ChangeSetElementRefs(
              updated = Seq(
                newChangeSetElementRef(1001, "01"),
              )
            ),
          )
        )
      )
    )
  }
}
