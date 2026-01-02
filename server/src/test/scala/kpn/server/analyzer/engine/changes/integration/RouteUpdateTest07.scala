package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.Fact.RouteBroken
import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotContinious
import kpn.api.common.Fact.RouteNotForward
import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newMember

class RouteUpdateTest07 extends IntegrationTest {

  test("fact diff") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .node(1003)
      .node(1004)
      .way(101, 1001, 1002)
      .way(102, 1003, 1003)
      .route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .node(1003)
      .node(1004)
      .way(101, 1001, 1002)
      .way(102, 1003, 1003)
      .route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101),
          newMember(MemberType.Way, 102)
        )
      )

    testIntegration(dataBefore, dataAfter) {
      processRelation(ChangeAction.Modify, dataAfter.rawRelationWithId(11))
      val baseRouteChange = findBaseRouteChangeById("1:1:11")
      assertEqual(
        baseRouteChange.routeDiff.factDiffs.get.introduced.toSet,
        Set(
          RouteNotBackward,
          RouteNotForward,
          RouteBroken,
          RouteNotContinious,
        )
      )
    }
  }
}
