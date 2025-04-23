package kpn.server.analyzer.engine.changes.integration

import kpn.api.common.changes.ChangeAction
import kpn.api.common.data.MemberType
import kpn.api.common.diff.TagDetail
import kpn.api.common.diff.TagDetailType
import kpn.api.common.diff.TagDiffs
import kpn.api.common.diff.route.RouteNameDiff
import kpn.core.test.OverpassData

class RouteUpdateTest05 extends IntegrationTest {

  test("name diff") {

    val dataBefore = OverpassData()
      .node(1001)
      .node(1002)
      .way(101, 1001, 1002)
      .route(
        11,
        "01-02", // <--
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
      .networkRelation(1, "name", Seq(newMember(MemberType.Relation, 11)))

    val dataAfter = OverpassData()
      .node(1001)
      .node(1002)
      .way(101, 1001, 1002)
      .route(
        11,
        "02-01", // <--
        Seq(
          newMember(MemberType.Way, 101)
        )
      )
      .networkRelation(1, "name", Seq(newMember(MemberType.Relation, 11)))

    testIntegration(dataBefore, dataAfter) {

      process(ChangeAction.Modify, dataAfter.rawRelationWithId(11))

      val routeChange = findRouteChangeById("123:1:11")

      assertEqual(
        routeChange.diffs.nameDiff,
        Some(
          RouteNameDiff("01-02", "02-01")
        )
      )

      assertEqual(
        routeChange.diffs.tagDiffs,
        Some(
          TagDiffs(
            Seq(
              TagDetail(TagDetailType.Update, "ref", Some("01-02"), Some("02-01")),
              TagDetail(TagDetailType.Same, "network", Some("rwn"), Some("rwn")),
              TagDetail(TagDetailType.Same, "type", Some("route"), Some("route")),
              TagDetail(TagDetailType.Same, "route", Some("foot"), Some("foot")),
              TagDetail(TagDetailType.Same, "network:type", Some("node_network"), Some("node_network"))
            ),
            Seq.empty
          )
        )
      )
    }
  }
}
