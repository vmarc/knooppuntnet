package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData

class RouteUpdateTest07 extends IntegrationTest {

  test("fact diff") {

    pending

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(
        11,
        "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      .networkRelation(1, "name", Seq(newMember("relation", 11)))

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .relation(12) // extra relation that does not belong in a route relation
      .route(
        11,
        "01-02",
        Seq(
          newMember("way", 101),
          newMember("relation", 12)
        )
      )
      .networkRelation(1, "name", Seq(newMember("relation", 11)))

    testIntegration(dataBefore, dataAfter) {
      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))
      val routeChange = findRouteChangeById("123:1:11")
    }

    //    val analysis = new NetworkRouteDiffAnalyzer(snapshot(before), snapshot(after), 11).analysis
    //
    //    val expectedDiff = FactDiffs(
    //      Set(),
    //      Set(
    //        Fact.RouteUnexpectedRelation,
    //        Fact.RouteBroken
    //      ),
    //      Set()
    //    )
    //
    //    analysis.get.diffs should matchTo(RouteDiff(factDiffs = Some(expectedDiff)))
  }
}
