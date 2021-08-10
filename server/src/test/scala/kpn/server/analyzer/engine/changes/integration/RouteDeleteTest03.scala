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
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.api.common.route.RouteInfo
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class RouteDeleteTest03 extends AbstractTest {

  test("orphan route looses route tags") {

    pending

    withDatabase { database =>

      val dataBefore = OverpassData()
        .networkNode(1001, "01")
        .networkNode(1002, "02")
        .way(101, 1001, 1002)
        .route(11, "01-02",
          Seq(
            newMember("way", 101)
          )
        )

      val dataAfter = OverpassData()
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

      val tc = new TestContext(database, dataBefore, dataAfter)
      tc.watchOrphanRoute(tc.before, 11)

      tc.process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))

      assert(!tc.analysisContext.data.routes.watched.contains(11))

      (tc.routeRepository.save _).verify(
        where { routeInfo: RouteInfo =>
          routeInfo.id should equal(11)
          assert(routeInfo.active)
          // assert(routeInfo.orphan)
          true
        }
      ).once()

      tc.findChangeSetSummaryById("123:1") should matchTo(
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

      tc.findRouteChangeById("123:1:11") should matchTo(
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
                  "route" -> "foot", // this is removed in 'after' situation
                  "note" -> "01-02",
                  "network:type" -> "node_network"
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
                  "type" -> "route", // route=foot is missing
                  "note" -> "01-02",
                  "network:type" -> "node_network"
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
                  TagDetail(TagDetailType.Same, "note", Some("01-02"), Some("01-02")),
                  TagDetail(TagDetailType.Same, "network", Some("rwn"), Some("rwn")),
                  TagDetail(TagDetailType.Same, "type", Some("route"), Some("route")),
                  TagDetail(TagDetailType.Delete, "route", Some("foot"), None), // <--
                  TagDetail(TagDetailType.Same, "network:type", Some("node_network"), Some("node_network"))
                )
              )
            )
          ),
          facts = Seq(Fact.WasOrphan, Fact.LostRouteTags),
          investigate = true,
          impact = true,
          locationInvestigate = true,
          locationImpact = true
        )
      )
    }
  }
}
