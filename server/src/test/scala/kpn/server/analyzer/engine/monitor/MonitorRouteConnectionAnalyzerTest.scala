package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember
import kpn.api.custom.Relation
import kpn.core.util.UnitTest

class MonitorRouteConnectionAnalyzerTest extends UnitTest {

  test("ways sorted") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2)
        memberWay(11, "", 2, 3)
      }
    ).shouldMatchTo(
      Seq(1, 2, 3)
    )
  }

  test("way 1 reversed") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 2, 1)
        memberWay(11, "", 2, 3)
      }
    ).shouldMatchTo(
      Seq(1, 2, 3)
    )
  }

  test("way 2 reversed") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2)
        memberWay(11, "", 3, 2)
      }
    ).shouldMatchTo(
      Seq(1, 2, 3)
    )
  }

  test("both ways forward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "forward", 1, 2)
        memberWay(11, "forward", 2, 3)
      }
    ).shouldMatchTo(
      Seq(1, 2, 3)
    )
  }

  test("way 1 forward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "forward", 1, 2)
        memberWay(11, "", 3, 2)
      }
    ).shouldMatchTo(
      Seq(1, 2, 3)
    )
  }

  test("way 2 forward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 2, 1)
        memberWay(11, "forward", 2, 3)
      }
    ).shouldMatchTo(
      Seq(1, 2, 3)
    )
  }

  test("both ways backward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "backward", 2, 1)
        memberWay(11, "backward", 3, 2)
      }
    ).shouldMatchTo(
      Seq(1, 2, 3)
    )
  }

  test("way 1 backward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "backward", 2, 1)
        memberWay(11, "", 3, 2)
      }
    ).shouldMatchTo(
      Seq(1, 2, 3)
    )
  }

  test("way 2 backward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 2, 1)
        memberWay(11, "backward", 3, 2)
      }
    ).shouldMatchTo(
      Seq(1, 2, 3)
    )
  }

  test("gap") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2)
        memberWay(11, "", 3, 4)
      }
    ) shouldBe empty
  }

  test("gap - both ways forward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "forward", 1, 2)
        memberWay(11, "forward", 3, 2)
      }
    ) shouldBe empty
  }

  test("gap - way 1 forward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "forward", 1, 2)
        memberWay(11, "", 1, 3)
      }
    ) shouldBe empty
  }

  test("gap - way 2 forward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2)
        memberWay(11, "forward", 3, 2)
      }
    ) shouldBe empty
  }

  test("gap - both ways backward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "backward", 2, 1)
        memberWay(11, "backward", 2, 3)
      }
    ) shouldBe empty
  }

  test("gap - way 1 backward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "backward", 1, 2)
        memberWay(11, "backward", 3, 2)
      }
    ) shouldBe empty
  }

  test("gap - way 2 backward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2)
        memberWay(11, "backward", 1, 3)
      }
    ) shouldBe empty
  }

  private def analyze(data: MonitorRouteTestData): Seq[Long] = {
    val relation = data.relation
    val wayMember1 = relationWayMember(relation, 0)
    val wayMember2 = relationWayMember(relation, 1)
    val routeWays = new MonitorRouteConnectionAnalyzer().analyze(wayMember1, wayMember2)
    routeWays.zipWithIndex.flatMap { case (routeWay, index) =>
      if (index == 0) routeWay.nodeIds else routeWay.nodeIds.tail
    }
  }

  private def relationWayMember(relation: Relation, index: Int): WayMember = {
    relation.members(index) match {
      case m: WayMember => m
      case _ => throw new IllegalStateException()
    }
  }
}
