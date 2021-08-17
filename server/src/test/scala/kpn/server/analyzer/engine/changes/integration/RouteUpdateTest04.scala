package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.common.diff.WayUpdate
import kpn.core.test.OverpassData

class RouteUpdateTest04 extends IntegrationTest {

  test("updated way") {

    val dataBefore = OverpassData()
      .node(1001)
      .node(1002)
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
      .node(1001)
      .node(1002)
      .way(101, 1002, 1001) // direction reversed
      .route(
        11,
        "01-02",
        Seq(
          newMember("way", 101)
        )
      )
      .networkRelation(1, "name", Seq(newMember("relation", 11)))

    testIntegration(dataBefore, dataAfter) {
      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))
      val routeChange = findRouteChangeById("123:1:11")
      routeChange.updatedWays should matchTo(
        Seq(
          WayUpdate(
            101,
            before = newMetaData(),
            after = newMetaData(),
            directionReversed = true
          )
        )
      )
    }
  }
}
