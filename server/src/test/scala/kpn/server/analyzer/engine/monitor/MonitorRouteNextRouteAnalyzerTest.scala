package kpn.server.analyzer.engine.monitor

import kpn.api.common.data.WayMember
import kpn.core.util.UnitTest

class MonitorRouteNextRouteAnalyzerTest extends UnitTest {

  test("connection") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2)
      }
    ).shouldMatchTo(Some(false))
  }

  test("connection to way with forward role") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "forward", 1, 2)
      }
    ).shouldMatchTo(Some(false))
  }

  test("connection to way with backward role") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "backward", 2, 1)
      }
    ).shouldMatchTo(Some(true))
  }

  test("gap") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 2, 3)
      }
    ).shouldMatchTo(None)
  }

  test("gap - way with role forward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "forward", 2, 1)
      }
    ).shouldMatchTo(None)
  }

  test("gap - way with role backward") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "backward", 1, 2)
      }
    ).shouldMatchTo(None)
  }

  private def analyze(data: MonitorRouteTestData): Option[Boolean] = {
    val relation = data.relation
    val wayMember = relation.members.head match {
      case m: WayMember => m
      case _ => throw new IllegalStateException()
    }
    val routeWayOption = new MonitorRouteNextRouteAnalyzer().analyze(1, wayMember)
    routeWayOption.map(_.reversed)
  }
}
