package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.Member
import kpn.api.custom.Tags
import kpn.core.test.TestObjects.newWay
import kpn.core.util.UnitTest

class MonitorRouteWayFilterTest extends UnitTest {

  test("filter") {

    val wayMembers = MonitorFilter.filterWayMembers(
      Seq(
        Member(way = Some(newWay(1))),
        Member(way = Some(newWay(2)), role = Some("place_of_worship")),
        Member(way = Some(newWay(3)), role = Some("guest_house")),
        Member(way = Some(newWay(4)), role = Some("outer")),
        Member(way = Some(newWay(5)), role = Some("inner")),
        Member(way = Some(newWay(6)), role = Some("random_other_role")),
        Member(way = Some(newWay(7, tags = Tags.from("building" -> "yes")))),
        Member(way = Some(newWay(8, tags = Tags.from("building" -> "church")))),
      )
    )

    wayMembers.map(_.memberId) should equal(Seq(1, 6))
  }
}
