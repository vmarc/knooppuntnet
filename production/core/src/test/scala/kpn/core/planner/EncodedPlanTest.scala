package kpn.core.planner

import org.scalatest.FunSuite
import org.scalatest.Matchers

class EncodedPlanTest extends FunSuite with Matchers {

  test("convert encoded plan string to object representation") {
    EncodedPlan("N1-R2-I3-R4-N5").items should equal(
      Seq(
        EncodedNode(1),
        EncodedRoute(2),
        EncodedIntermediateNode(3),
        EncodedRoute(4),
        EncodedNode(5)
      )
    )
  }

  test("plan with only start node") {
    EncodedPlan("N1").items should equal(
      Seq(
        EncodedNode(1)
      )
    )
  }

  test("empty string results in empty plan") {
    EncodedPlan("").items.size should equal(0)
    EncodedPlan("  ").items.size should equal(0)
  }

  test("invalid syntax") {
    intercept[EncodedPlanException] {
      EncodedPlan("N1-A2")
    }.getMessage should equal("invalid syntax for encoded item 'A2' in encoded plan 'N1-A2'")

    intercept[EncodedPlanException] {
      EncodedPlan("bla")
    }.getMessage should equal("invalid syntax for encoded item 'bla' in encoded plan 'bla'")
  }

  test("userNodeIds does not include the ids of the intermediate nodes") {
    EncodedPlan("N1-R2-I3-R4-N5").userNodeIds should equal(Seq(1L, 5L))
  }

  test("allNodeIds includes the ids both both user nodes and intermediate nodes") {
    EncodedPlan("N1-R2-I3-R4-N5").allNodeIds should equal(Seq(1L, 3L, 5L))
  }

  test("routeIds") {
    EncodedPlan("N1-R2-I3-R4-N5").routeIds should equal(Seq(2L, 4L))
  }
}
