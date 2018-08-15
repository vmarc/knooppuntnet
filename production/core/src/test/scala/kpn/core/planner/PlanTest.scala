package kpn.core.planner

import org.scalatest.FunSuite
import org.scalatest.Matchers

class PlanTest extends FunSuite with Matchers {

  test("plan encoding") {
    val items = Seq(
      PlanNode(1, "01", 0),
      PlanRoute(2, "01-03", 100),
      PlanIntermediateNode(3, "03", 100),
      PlanRoute(4, "03-05", 200),
      PlanNode(5, "05", 200)
    )
    Plan(items, "").encoded should equal("N1-R2-I3-R4-N5")
  }

  test("messages are not included in encoded plan") {
    val items = Seq(
      PlanNode(1, "01", 0),
      PlanMessageItem(RouteNotFoundInDatabase(2)),
      PlanNode(3, "03", 0)
    )
    Plan(items, "").encoded should equal("N1-N3")
  }
}
