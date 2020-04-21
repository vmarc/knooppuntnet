package kpn.server.analyzer.engine.analysis.route

import kpn.api.custom.Tags
import org.scalatest.FunSuite
import org.scalatest.matchers.should.Matchers

class BreakPointAnalysisTest extends FunSuite with Matchers {

  test("continuous") {

    val d = new RouteTestData("01-02") {
      way(10, 1, 2, 3)
      way(11, 3, 4, 5)
      way(12, 5, 6, 7)
    }

    assertNotBroken(d)
  }

  test("broken") {

    val d = new RouteTestData("01-02") {
      way(10, 1, 2)
      way(11, 3, 4)
      way(12, 4, 5)
    }

    assertBrokenAt(d, 11, 3)
  }

  test("broken, with nodes reversed") {

    val d = new RouteTestData("01-02") {
      way(10, 1, 2)
      way(11, 4, 3)
      way(12, 4, 5)
    }

    assertBrokenAt(d, 11, 3)
  }

  test("broken at last way") {

    val d = new RouteTestData("01-02") {
      way(10, 1, 2)
      way(11, 3, 4)
    }

    assertBrokenAt(d, 11, 3)
  }

  test("ways without nodes are skipped") { // ways without nodes are reported separately

    val d = new RouteTestData("01-02") {
      way(10, 1, 2)
      way(11)
      way(12, 2, 3)
    }

    assertNotBroken(d)
  }

  test("first way 'overshoots' (common node in the middle, but no common endnodes)") {

    val d = new RouteTestData("01-02") {
      way(10, 1, 2, 3)
      way(11, 2, 4)
    }

    assertBrokenAt(d, 11, 2)
  }

  test("roundabout continuous") {

    val d = new RouteTestData("01-02") {
      way(10, 1, 3)
      way(11, Tags.from("junction" -> "roundabout"), 2, 3, 4, 5, 6)
      way(12, 5, 7)
    }

    assertNotBroken(d) // continuous path over nodes 1, 3, 4, 5, 7
  }

  test("broken at roundabout") {

    val d = new RouteTestData("01-02") {
      way(10, 1, 2)
      way(11, Tags.from("junction" -> "roundabout"), 3, 4, 5, 6)
      way(12, 5, 7)
    }

    assertBrokenAt(d, 10, 2)
  }

  test("broken after roundabout") {

    val d = new RouteTestData("01-02") {
      way(10, 1, 3)
      way(11, Tags.from("junction" -> "roundabout"), 2, 3, 4, 5, 6)
      way(12, 7, 8)
    }

    assertBrokenAt(d, 12, 7)
  }

  test("continuous through closed loop") {

    val d = new RouteTestData("01-02") {
      way(10, 1, 3)
      way(11, 2, 3, 4, 5, 6, 2)
      way(12, 5, 7)
    }

    assertNotBroken(d) // continuous path over nodes 1, 3, 4, 5, 7
  }

  test("broken at closed loop") {

    val d = new RouteTestData("01-02") {
      way(10, 1, 2)
      way(11, 3, 4, 5, 6, 3)
      way(12, 5, 7)
    }

    assertBrokenAt(d, 10, 2)
  }

  test("single way cannot be broken") {

    val d = new RouteTestData("01-02") {
      way(10, 1, 2, 3)
    }

    assertNotBroken(d)
  }

  private def assertNotBroken(d: RouteTestData): Unit = {
    new BreakPointAnalysis().breakPoint(d.data.ways.values.toSeq) should equal(None)
  }

  private def assertBrokenAt(d: RouteTestData, wayId: Int, nodeId: Int): Unit = {
    val breakPoint = new BreakPointAnalysis().breakPoint(d.data.ways.values.toSeq).get
    breakPoint.way.id.toInt should equal(wayId)
    breakPoint.node.id.toInt should equal(nodeId)
  }
}
