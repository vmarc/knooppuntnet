package kpn.core.planner

import org.scalatest.FunSuite
import org.scalatest.Matchers

import scalax.collection.edge.Implicits._
import scalax.collection.edge.WLUnDiEdge
import scalax.collection.immutable.Graph

class PlanUpdaterTest extends FunSuite with Matchers {

  test("add first node") {
    val graph = Graph[Long, WLUnDiEdge]()
    val before = EncodedPlan("")
    val after = EncodedPlan("N1")
    new PlanUpdater(graph).add(before, 1) should equal(after)
  }

  test("add route to other node with intermediate nodes") {
    val graph = Graph[Long, WLUnDiEdge](
      (1L ~%+ 2L)(1L, 10L),
      (2L ~%+ 3L)(1L, 20L),
      (3L ~%+ 4L)(1L, 30L)
    )
    val before = EncodedPlan("N1")
    val after = EncodedPlan("N1-R10-I2-R20-I3-R30-N4")
    new PlanUpdater(graph).add(before, 4) should equal(after)
  }

  test("start node not found in graph") {
    val graph = Graph[Long, WLUnDiEdge]()
    val before = EncodedPlan("N1")
    val after = EncodedPlan(Seq(EncodedNode(1), EncodedMessage(StartNodeNotFoundInGraph(1, 4))))
    new PlanUpdater(graph).add(before, 4) should equal(after)
  }

  test("end node not found in graph") {
    val graph = Graph[Long, WLUnDiEdge](
      (1L ~%+ 2L)(1, 10)
    )
    val before = EncodedPlan("N1")
    val after = EncodedPlan(Seq(EncodedNode(1), EncodedMessage(EndNodeNotFoundInGraph(1, 4))))
    new PlanUpdater(graph).add(before, 4) should equal(after)
  }

  test("path not found in graph") {
    val graph = Graph[Long, WLUnDiEdge](
      (1L ~%+ 2L)(1L, "10"),
      (3L ~%+ 4L)(1L, "30")
    )
    val before = EncodedPlan("N1")
    val after: EncodedPlan = new PlanUpdater(graph).add(before, 4)
    after.items should equal(Seq(EncodedNode(1), EncodedMessage(PathNotFoundInGraph(1, 4))))
  }

  test("undo") {
    undo("", "")
    undo("N1", "")
    undo("N1-R2-N3-R4-I5-R6-N7", "N1-R2-N3")
    undo("N1-R2-N3-R4-N5-R6-N7", "N1-R2-N3-R4-N5")
  }

  private def undo(before: String, after: String): Unit = {
    val graph = Graph[Long, WLUnDiEdge]()
    new PlanUpdater(graph).undo(EncodedPlan(before)) should equal(EncodedPlan(after))
  }
}
