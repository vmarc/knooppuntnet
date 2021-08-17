package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.core.test.OverpassData

class RouteUpdateTest06 extends IntegrationTest {

  test("role diff") { // TODO MONGO do we still want this? move to networkUpdate test?

    pending

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
      .networkRelation(1, "name", Seq(newMember("relation", 11, "role")))

    val dataAfter = OverpassData()
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
      .networkRelation(1, "name", Seq(newMember("relation", 11, "connection")))

    testIntegration(dataBefore, dataAfter) {
      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))
      val routeChange = findRouteChangeById("123:1:11")
    }

    //    val analysis = new NetworkRouteDiffAnalyzer(snapshot(before), snapshot(after), 11).analysis
    //    analysis.get.diffs should matchTo(RouteDiff(roleDiff = Some(RouteRoleDiff(Some("role"), Some("connection")))))
  }

}
