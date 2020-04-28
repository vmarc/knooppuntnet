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
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.route.RouteInfo

class OrphanRouteTest05 extends AbstractTest {

  test("orphan route looses route tags") {

    val dataBefore = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(11, "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      .data

    val dataAfter = TestData2()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .relation(
        11,
        Seq(
          newMember("way", 101)
        ),
        Tags.from("network" -> "rwn", "type" -> "route", "note" -> "01-02", "network:type" -> "node_network")
      )
      .data

    val tc = new TestConfig()

    tc.relationBefore(dataBefore, 11)
    tc.watchOrphanRoute(dataBefore, 11)
    tc.relationAfter(dataAfter, 11)
    tc.process(ChangeAction.Modify, TestData.relation(dataAfter, 11))

    tc.analysisContext.data.orphanRoutes.watched.contains(11) should equal(false)

    (tc.analysisRepository.saveRoute _).verify(
      where { routeInfo: RouteInfo =>
        routeInfo.id should equal(11)
        routeInfo.active should equal(true)
        routeInfo.orphan should equal(true)
        true
      }
    ).once()

    (tc.changeSetRepository.saveChangeSetSummary _).verify(
      where { changeSetSummary: ChangeSetSummary =>
        changeSetSummary should equal(
          newChangeSetSummary(
            subsets = Seq(Subset.nlHiking),
            orphanRouteChanges = Seq(
              ChangeSetSubsetElementRefs(
                Subset.nlHiking,
                ChangeSetElementRefs(
                  updated = Seq(newChangeSetElementRef(11, "01-02", investigate = true))
                )
              )
            ),
            subsetAnalyses = Seq(
              ChangeSetSubsetAnalysis(Subset.nlHiking, investigate = true)
            ),
            investigate = true
          )
        )
        true
      }
    )

    (tc.changeSetRepository.saveRouteChange _).verify(
      where { routeChange: RouteChange =>
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
                    "route" -> "foot", // this is removed in 'after' situation
                    "note" -> "01-02",
                    "network:type" -> "node_network"
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
                    "type" -> "route", // route=foot is missing
                    "note" -> "01-02",
                    "network:type" -> "node_network"
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
                ),
                facts = Seq(
                  Fact.RouteTagMissing,
                  Fact.RouteBroken
                )
              )
            ),
            diffs = RouteDiff(
              factDiffs = Some(
                FactDiffs(
                  introduced = Set(
                    Fact.RouteTagMissing,
                    Fact.RouteBroken
                  )
                )
              ),
              tagDiffs = Some(
                TagDiffs(
                  mainTags = Seq(
                    TagDetail(TagDetailType.Same, "network", Some("rwn"), Some("rwn")),
                    TagDetail(TagDetailType.Same, "type", Some("route"), Some("route")),
                    TagDetail(TagDetailType.Delete, "route", Some("foot"), None), // <--
                    TagDetail(TagDetailType.Same, "note", Some("01-02"), Some("01-02")),
                    TagDetail(TagDetailType.Same, "network:type", Some("node_network"), Some("node_network"))
                  )
                )
              )
            ),
            facts = Seq(Fact.WasOrphan, Fact.LostRouteTags),
            investigate = true,
            locationInvestigate = true
          )
        )
        true
      }
    )
  }
}
