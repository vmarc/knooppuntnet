package kpn.api.common.planner

import kpn.api.common.common.TrackPathKey
import kpn.core.util.UnitTest

class LegEndTest extends UnitTest {

  test("start node only") {
    assertEqual(LegEnd.fromPlanString("a"), Seq(LegEnd.node(10)))
  }

  test("node-node") {
    assertEqual(
      LegEnd.fromPlanString("a-b"),
      Seq(
        LegEnd.node(10),
        LegEnd.node(11)
      )
    )
  }

  test("node-node-node") {
    assertEqual(
      LegEnd.fromPlanString("a-b-c"),
      Seq(
        LegEnd.node(10),
        LegEnd.node(11),
        LegEnd.node(12)
      )
    )
  }

  test("node-route") {
    assertEqual(
      LegEnd.fromPlanString("a-b.1"),
      Seq(
        LegEnd.node(10),
        LegEnd.route(List(TrackPathKey(11, 1)))
      )
    )
  }

  test("node-route-node") {
    assertEqual(
      LegEnd.fromPlanString("a-b.1-c"),
      Seq(
        LegEnd.node(10),
        LegEnd.route(List(TrackPathKey(11, 1))),
        LegEnd.node(12)
      )
    )
  }

  test("route-node") {
    assertEqual(
      LegEnd.fromPlanString("a.1-b"),
      Seq(
        LegEnd.route(List(TrackPathKey(10, 1))),
        LegEnd.node(11)
      )
    )
  }

  test("invalid radix36 values") {
    intercept[IllegalArgumentException] {
      LegEnd.fromPlanString("a-?-c")
    }.getMessage should equal("Could not interprete planString 'a-?-c'")
  }

  test("node-routes") {
    assertEqual(
      LegEnd.fromPlanString("a-b.1|c.1"),
      Seq(
        LegEnd.node(10),
        LegEnd.route(List(TrackPathKey(11, 1), TrackPathKey(12, 1))),
      )
    )
  }
}
