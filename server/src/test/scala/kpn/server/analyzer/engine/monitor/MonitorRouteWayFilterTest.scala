package kpn.server.analyzer.engine.monitor

import kpn.api.common.SharedTestObjects
import kpn.api.common.data.WayMember
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

class MonitorRouteWayFilterTest extends UnitTest with SharedTestObjects {

  test("filter") {

    val wayMembers = MonitorFilter.filterWayMembers(
      Seq(
        WayMember(newWay(1), None),
        WayMember(newWay(2), Some("place_of_worship")),
        WayMember(newWay(3), Some("guest_house")),
        WayMember(newWay(4), Some("outer")),
        WayMember(newWay(5), Some("inner")),
        WayMember(newWay(6), Some("random_other_role")),
        WayMember(newWay(7, tags = Tags.from("building" -> "yes")), None),
        WayMember(newWay(8, tags = Tags.from("building" -> "church")), None),
      )
    )

    wayMembers.map(_.way.id) should equal(Seq(1, 6))
  }

}
