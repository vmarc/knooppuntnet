package kpn.api.common.planner

import kpn.core.util.UnitTest

class LegEndTest extends UnitTest {

  test("start node only") {
    LegEnd.fromPlanString("a") should equal(Seq(LegEnd.node(10)))
  }

  test("node-node") {
    LegEnd.fromPlanString("a-b") should equal(
      Seq(
        LegEnd.node(10),
        LegEnd.node(11)
      )
    )
  }

  test("node-node-node") {
    LegEnd.fromPlanString("a-b-c") should equal(
      Seq(
        LegEnd.node(10),
        LegEnd.node(11),
        LegEnd.node(12)
      )
    )
  }

  test("node-route") {
    LegEnd.fromPlanString("a-b.1") should equal(
      Seq(
        LegEnd.node(10),
        LegEnd.route(11, 1)
      )
    )
  }

  test("node-route-node") {
    LegEnd.fromPlanString("a-b.1-c") should equal(
      Seq(
        LegEnd.node(10),
        LegEnd.route(11, 1),
        LegEnd.node(12)
      )
    )
  }

  test("route-node") {
    LegEnd.fromPlanString("a.1-b") should equal(
      Seq(
        LegEnd.route(10, 1),
        LegEnd.node(11)
      )
    )
  }

  test("invalid radix36 values") {
    intercept[IllegalArgumentException] {
      LegEnd.fromPlanString("a-?-c")
    }.getMessage should equal("Could not interprete planString 'a-?-c'")
  }

}
