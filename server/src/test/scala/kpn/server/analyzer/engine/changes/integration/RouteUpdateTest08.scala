package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.Fact
import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.api.common.diff.common.FactDiffs
import kpn.api.common.diff.route.RouteDiff
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newMember

class RouteUpdateTest08 extends IntegrationTest {

  test("unexpected relation member added to route relation") {

    val dataBefore = OverpassData()
      .node(1001)
      .node(1002)
      .node(1003)
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
      .route(
        11,
        "route-11",
        Seq(
          newMember(MemberType.Way, 101),
        ),
        version = 1
      )
      .route(
        12,
        "route-12",
        Seq(
          newMember(MemberType.Way, 102),
          newMember(MemberType.Relation, 11)
        ),
        version = 1
      )

    val dataAfter = OverpassData()
      .node(1001)
      .node(1002)
      .node(1003)
      .way(101, 1001, 1002)
      .way(102, 1002, 1003)
      .relation(13) // extra relation that does not belong in a route relation
      .route(
        11,
        "route-11",
        Seq(
          newMember(MemberType.Way, 101),
        ),
        version = 1
      )
      .route(
        12,
        "route-12",
        Seq(
          newMember(MemberType.Way, 102),
          newMember(MemberType.Relation, 11),
          newMember(MemberType.Relation, 13)
        ),
        version = 2
      )

    testIntegration(dataBefore, dataAfter) {
      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))
      val routeChange = findRouteChangeById("123:1:11")
      assertEqual(
        routeChange.diffs,
        RouteDiff(
          factDiffs = Some(
            FactDiffs(
              introduced = Seq(
                Fact.RouteUnexpectedRelation,
                Fact.RouteBroken
              )
            )
          )
        )
      )
    }
  }
}
