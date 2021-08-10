package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.ChangeSetElementRefs
import kpn.api.common.ChangeSetSubsetAnalysis
import kpn.api.common.ChangeSetSubsetElementRefs
import kpn.api.common.changes.ChangeAction
import kpn.api.common.changes.details.ChangeType
import kpn.api.common.data.raw.RawMember
import kpn.api.common.route.RouteInfo
import kpn.api.custom.Country
import kpn.api.custom.Fact
import kpn.api.custom.NetworkType
import kpn.api.custom.Subset
import kpn.api.custom.Tags
import kpn.core.test.OverpassData
import kpn.core.test.TestSupport.withDatabase

class RouteDeleteTest01 extends AbstractTest {

  test("delete orphan route") {

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

      val dataAfter = OverpassData.empty

      val tc = new TestContext(database, dataBefore, dataAfter)
      tc.watchOrphanRoute(tc.before, 11)

      tc.process(ChangeAction.Delete, newRawRelation(11))

      assert(!tc.analysisContext.data.routes.watched.contains(11))

      (tc.routeRepository.save _).verify(
        where { routeInfo: RouteInfo =>
          routeInfo.id should equal(11)
          // assert(routeInfo.orphan)
          assert(!routeInfo.active)
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
                removed = Seq(newChangeSetElementRef(11, "01-02", investigate = true))
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
          ChangeType.Delete,
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
                tags = newRouteTags("01-02")
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
