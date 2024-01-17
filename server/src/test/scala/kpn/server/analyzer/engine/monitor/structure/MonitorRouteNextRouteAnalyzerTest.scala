package kpn.server.analyzer.engine.monitor.structure

import kpn.api.common.data.WayMember
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.monitor.MonitorRouteTestData

class MonitorRouteNextRouteAnalyzerTest extends UnitTest {

  test("connection") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2)
      }
    ).shouldMatchTo(Some("1>2"))
  }

  test("connection to way with forward role") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "forward", 1, 2)
      }
    ).shouldMatchTo(Some("1>2 (oneWay)"))
  }

  test("connection to way with backward role") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "backward", 2, 1)
      }
    ).shouldMatchTo(Some("1>2 (oneWay)"))
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

  private def analyze(data: MonitorRouteTestData): Option[String] = {
    val relation = data.relation
    val wayMember = relation.members.head match {
      case m: WayMember => m
      case _ => throw new IllegalStateException()
    }
    new MonitorRouteNextRouteAnalyzer().analyze(1, wayMember).map(_.string)
  }
}
