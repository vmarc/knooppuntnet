package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.Fact
import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.api.common.diff.common.FactDiffs
import kpn.core.test.OverpassData

class RouteUpdateTest07 extends IntegrationTest {

  test("fact diff") {

    // pendingRedesignPrio2() // TODO redesign - need better analysis to determine whether a subrelation is a route or not

    val dataBefore = OverpassData()
      .networkNode(1001, "01")
      .networkNode(1002, "02")
      .way(101, 1001, 1002)
      .route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
      .networkRelation(1, "name", Seq(newMember(MemberType.Relation, 11)))

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
        )
      )
      .networkRelation(1, "name", Seq(newMember(MemberType.Relation, 11)))

    testIntegration(dataBefore, dataAfter) {
      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))
      val routeChange = findRouteChangeById("123:1:11")
      assertEqual(
        routeChange.diffs.factDiffs,
        Some(
          FactDiffs(
            introduced = Seq(
              Fact.RouteUnexpectedRelation,
              Fact.RouteBroken
            )
          )
        )
      )
    }
  }
}
