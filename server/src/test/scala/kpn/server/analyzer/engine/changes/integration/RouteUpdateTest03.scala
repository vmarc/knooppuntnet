package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
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
          newMember(MemberType.Way, 101)
        )
      )
      .networkRelation(1, "name", Seq(newMember(MemberType.Relation, 11)))

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
          newMember(MemberType.Way, 101),
          newMember(MemberType.Way, 102)
        )
      )
      .networkRelation(1, "name", Seq(newMember(MemberType.Relation, 11)))

    testIntegration(dataBefore, dataAfter) {
      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))
      val baseRouteChange = findBaseRouteChangeById("123:1:11")
      assertEqual(
        baseRouteChange.wayDiffs.map(_.added),
        Some(
          Seq(
            newWayInfo(
              102,
              tags = Tags.from(
                "highway" -> "unclassified"
              )
            )
          )
        )
      )
    }
  }
}
