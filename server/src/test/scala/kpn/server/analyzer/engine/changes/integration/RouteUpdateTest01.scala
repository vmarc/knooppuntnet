package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.custom.Country
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData

class RouteUpdateTest01 extends IntegrationTest {

  test("update route") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02",
        Seq(
          newMember("way", 101)
        ),
        Tags.from("key" -> "value1")
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02",
        Seq(
          newMember("way", 101)
        ),
        Tags.from("key" -> "value2")
      )

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))

      assert(watched.routes.contains(11))

      assertRoute()
      assertRouteChange()
      assertOrphanRoute()
      assert(database.nodeChanges.isEmpty)
      assertChangeSetSummary()
    }
  }

  private def assertRoute(): Unit = {
    val routeInfo = findRouteById(11)
    routeInfo.id should equal(11)
    assert(routeInfo.active)
  }

  private def assertRouteChange(): Unit = {
    findRouteChangeById("123:1:11") should matchTo(
      newRouteChange(
        newChangeKey(elementId = 11),
        ChangeType.Update,
        "01-02",
        before = Some(
          newRouteData(
            Some(Country.nl),
            NetworkType.hiking,
            relation = newRawRelation(
              11,
              members = Seq(
                RawMember("way", 101, None)
              ),
              tags = Tags.from(
                "network" -> "rwn",
                "type" -> "route",
                "route" -> "foot",
                "note" -> "01-02",
                "network:type" -> "node_network",
                "key" -> "value1" // <--
              )
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
        ),
        after = Some(
          newRouteData(
            Some(Country.nl),
            NetworkType.hiking,
            relation = newRawRelation(
              11,
              members = Seq(
                RawMember("way", 101, None)
              ),
              tags = Tags.from(
                "network" -> "rwn",
                "type" -> "route",
                "route" -> "foot",
                "note" -> "01-02",
                "network:type" -> "node_network",
                "key" -> "value2" // <--
              )
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
        ),
        diffs = RouteDiff(
          tagDiffs = Some(
            TagDiffs(
              mainTags = Seq(
                TagDetail(TagDetailType.Same, "note", Some("01-02"), Some("01-02")),
                TagDetail(TagDetailType.Same, "network", Some("rwn"), Some("rwn")),
                TagDetail(TagDetailType.Same, "type", Some("route"), Some("route")),
                TagDetail(TagDetailType.Same, "route", Some("foot"), Some("foot")),
                TagDetail(TagDetailType.Same, "network:type", Some("node_network"), Some("node_network"))
              ),
              extraTags = Seq(
                TagDetail(TagDetailType.Update, "key", Some("value1"), Some("value2"))
              )
            )
          )
        ),
        impactedNodeIds = Seq(1001, 1002)
      )
    )
  }

  private def assertOrphanRoute(): Unit = {
    findOrphanRouteById(11L) should matchTo(
      newOrphanRouteDoc(
        11L,
        country = Country.nl,
        networkType = NetworkType.hiking,
        name = "01-02"
      )
    )
  }

  private def assertChangeSetSummary(): Unit = {
    findChangeSetSummaryById("123:1") should matchTo(
      newChangeSetSummary(
        subsets = Seq(Subset.nlHiking),
        routeChanges = Seq(
          ChangeSetSubsetElementRefs(
            Subset.nlHiking,
            ChangeSetElementRefs(
              updated = Seq(newChangeSetElementRef(11, "01-02"))
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
