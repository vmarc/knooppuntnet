package kpn.server.analyzer.engine.changes.integration

import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.TestData
import kpn.core.test.TestData2
import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.ChangeSetSummary
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.changes.details.RouteChange
import kpn.api.common.data.raw.RawMember
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.route.RouteInfo

class OrphanRouteTest02 extends AbstractTest {

  test("update orphan route") {

    pending

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02",
        Seq(
          newMember("way", 101)
        ),
        Tags.from("key" -> "value1")
      )
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02",
        Seq(
          newMember("way", 101)
        ),
        Tags.from("key" -> "value2")
      )
      .data

    val tc = new TestConfig()

    tc.relationBefore(dataBefore, 11)
    tc.watchOrphanRoute(dataBefore, 11)
    tc.relationAfter(dataAfter, 11)
    tc.process(ChangeAction.Modify, TestData.relation(dataAfter, 11))

    assert(tc.analysisContext.data.routes.watched.contains(11))

    (tc.routeRepository.save _).verify(
      where { routeInfo: RouteInfo =>
        routeInfo.id should equal(11)
        assert(routeInfo.active)
        // assert(routeInfo.orphan)
        true
      }
    ).once()

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should matchTo(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            orphanRouteChanges = Seq(
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
        true
      }
    )

    (tc.changeSetRepository.saveRouteChange _).verify(
      where { routeChange: RouteChange =>
        routeChange should matchTo(
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
            facts = Seq(Fact.OrphanRoute)
          )
        )
        true
      }
    )
  }
}
