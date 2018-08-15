package kpn.core.engine.changes.integration

import kpn.core.test.TestData
import kpn.core.test.TestData2
import kpn.shared.ChangeSetElementRef
import kpn.shared.ChangeSetElementRefs
import kpn.shared.ChangeSetSubsetElementRefs
import kpn.shared.ChangeSetSummary
import kpn.shared.Country
import kpn.shared.Fact
import kpn.shared.NetworkType
import kpn.shared.Subset
import kpn.shared.changes.ChangeAction
import kpn.shared.changes.details.ChangeType
import kpn.shared.changes.details.RouteChange
import kpn.shared.data.Tags
import kpn.shared.data.raw.RawMember
import kpn.shared.diff.RouteData
import kpn.shared.diff.TagDetail
import kpn.shared.diff.TagDetailType
import kpn.shared.diff.TagDiffs
import kpn.shared.diff.route.RouteDiff
import kpn.shared.route.RouteInfo

class OrphanRouteTest02 extends AbstractTest {

  test("update orphan route") {

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

    tc.analysisData.orphanRoutes.watched.contains(11) should equal(true)
    tc.analysisData.orphanRoutes.ignored.contains(11) should equal(false)

    (tc.analysisRepository.saveRoute _).verify(
      where { (routeInfo: RouteInfo) =>
        routeInfo.id should equal(11)
        routeInfo.active should equal(true)
        routeInfo.orphan should equal(true)
        routeInfo.ignored should equal(false)
        true
      }
    ).once()

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { (changeSetSummary: ChangeSetSummary) =>
        changeSetSummary should equal(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            orphanRouteChanges = Seq(
              ChangeSetSubsetElementRefs(
                Subset.nlHiking,
                ChangeSetElementRefs(
                  updated = Seq(newChangeSetElementRef(11, "01-02"))
                )
              )
            )
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveRouteChange _).verify(
      where { (routeChange: RouteChange) =>
        routeChange should equal(
          newRouteChange(
            newChangeKey(elementId = 11),
            ChangeType.Update,
            "01-02",
            before = Some(
              newRouteData(
                Some(Country.nl),
                NetworkType.hiking,
                newRawRelation(
                  11,
                  members = Seq(
                    RawMember("way", 101, None)
                  ),
                  tags = Tags.from(
                    "network" -> "rwn",
                    "type" -> "route",
                    "route" -> "foot",
                    "note" -> "01-02",
                    "key" -> "value1" // <--
                  )
                ),
                "01-02",
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
                newRawRelation(
                  11,
                  members = Seq(
                    RawMember("way", 101, None)
                  ),
                  tags = Tags.from(
                    "network" -> "rwn",
                    "type" -> "route",
                    "route" -> "foot",
                    "note" -> "01-02",
                    "key" -> "value2" // <--
                  )
                ),
                "01-02",
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
                    TagDetail(TagDetailType.Same, "network", Some("rwn"), Some("rwn")),
                    TagDetail(TagDetailType.Same, "type", Some("route"), Some("route")),
                    TagDetail(TagDetailType.Same, "route", Some("foot"), Some("foot")),
                    TagDetail(TagDetailType.Same, "note", Some("01-02"), Some("01-02"))
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
