package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.Fact
import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.api.common.diff.common.FactDiffs
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newRouteDiff

class RouteUpdateTest08 extends IntegrationTest {

  test("unexpected relation member added to route relation") {

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101),
        ),
        version = 1
      )

    val dataAfter = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .relation(12) // extra relation that does not belong in a route relation
      .route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101),
          newMember(MemberType.Relation, 12)
        ),
        version = 2
      )

    testIntegration(dataBefore, dataAfter) {
      processRelation(ChangeAction.Modify, dataAfter.rawRelationWithId(11))
      val baseRouteChange = findBaseRouteChangeById("1:1:11")
      assertEqual(
        baseRouteChange.routeDiff,
        newRouteDiff(
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
