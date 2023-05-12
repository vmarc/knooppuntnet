package kpn.server.monitor

import kpn.api.common.SharedTestObjects
import kpn.core.util.UnitTest
import kpn.server.monitor.domain.MonitorRoute

class MonitorUtilTest extends UnitTest with SharedTestObjects {

  test("subRelation") {

    val group = newMonitorGroup("group")
    val route = newMonitorRoute(
      group._id,
      "route",
      relation = Some(
        newMonitorRouteRelation(
          1L,
          "subrelation 1",
          relations = Seq(
            newMonitorRouteRelation(
              11L,
              "subrelation 11",
            ),
            newMonitorRouteRelation(
              12L,
              "subrelation 12",
              relations = Seq(
                newMonitorRouteRelation(
                  121L,
                  "subrelation 121",
                ),
                newMonitorRouteRelation(
                  122L,
                  "subrelation 122",
                )
              )
            )
          )
        )
      )
    )

    subRelation(route, 1) should equal(Some("subrelation 1"))
    subRelation(route, 11) should equal(Some("subrelation 11"))
    subRelation(route, 122) should equal(Some("subrelation 122"))
    subRelation(route, 99) should equal(None)

    MonitorUtil.subRelationsIn(route).map(_.name) should equal(
      Seq(
        "subrelation 11",
        "subrelation 12",
        "subrelation 121",
        "subrelation 122",
      )
    )
  }

  private def subRelation(route: MonitorRoute, subRelationId: Long): Option[String] = {
    MonitorUtil.subRelation(route, subRelationId).map(_.name)
  }
}
