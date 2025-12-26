package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.core.test.OverpassData
import kpn.core.test.TestObjects.newMember
import kpn.core.test.TestObjects.newMetaData
import kpn.core.test.TestObjects.newWayUpdate

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
      processRelation(ChangeAction.Modify, dataAfter.rawRelationWithId(11))
      val baseRouteChange = findBaseRouteChangeById("1:1:11")
      assertEqual(
        baseRouteChange.wayDiffs.map(_.updated).get,
        Seq(
          newWayUpdate(
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
