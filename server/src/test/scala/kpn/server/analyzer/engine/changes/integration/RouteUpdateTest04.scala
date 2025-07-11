package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.api.common.diff.WayUpdate
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMetaData

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
          newMember(MemberType.Way, 101)
        )
      )
      .networkRelation(1, "name", Seq(newMember(MemberType.Relation, 11)))

    val dataAfter = OverpassData()
      .node(1001)
      .node(1002)
      .way(101, 1002, 1001) // direction reversed
      .route(
        11,
        "01-02",
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
      .networkRelation(1, "name", Seq(newMember(MemberType.Relation, 11)))

    testIntegration(dataBefore, dataAfter) {
      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))
      val baseRouteChange = findBaseRouteChangeById("123:1:11")
      assertEqual(
        baseRouteChange.wayDiffs.map(_.updated),
        Some(
          Seq(
            WayUpdate(
              101,
              before = newMetaData(),
              after = newMetaData(),
              directionReversed = true
            )
          )
        )
      )
    }
  }
}
