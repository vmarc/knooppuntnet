package kpn.server.api.planner.leg

import kpn.api.common.common.TrackPathKey
import kpn.api.common.planner.LegEnd
import kpn.core.util.UnitTest

class PlanKeyTest extends UnitTest {

  test("legEnd") {
    PlanKey.legEnd(LegEnd.node(1001)) should equal("1001")
    PlanKey.legEnd(LegEnd.route(List(TrackPathKey(11, 3)))) should equal("11.3")
  }

  test("leg") {
    PlanKey.leg(LegEnd.node(1001), LegEnd.node(1002)) should equal("1001-1002")
    PlanKey.leg(LegEnd.node(1001), LegEnd.route(List(TrackPathKey(11, 1)))) should equal("1001-11.1")
    PlanKey.leg(LegEnd.route(List(TrackPathKey(11, 1))), LegEnd.node(1001)) should equal("11.1-1001")
    PlanKey.leg(LegEnd.route(List(TrackPathKey(11, 1))), LegEnd.route(List(TrackPathKey(12, 2)))) should equal("11.1-12.2")
  }

}
