package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.custom.Tags
import kpn.core.test.OverpassData

class RouteUpdateTest03 extends IntegrationTest {

  test("added way") {

    val dataBefore = OverpassData()
      .node(1001)
      .node(1002)
      .node(1003)
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
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
      .node(1003)
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
      .route(
        11,
        "01-02",
        Seq(
          newMember("way", 101),
          newMember("way", 102)
        )
      )
      .networkRelation(1, "name", Seq(newMember("relation", 11)))

    testIntegration(dataBefore, dataAfter) {
      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))
      val routeChange = findRouteChangeById("123:1:11")
      routeChange.addedWays should matchTo(
        Seq(
          newRawWay(
            102,
            nodeIds = Seq(1002, 1003),
            tags = Tags.from(
              "highway" -> "unclassified"
            )
          )
        )
      )
    }
  }
}
