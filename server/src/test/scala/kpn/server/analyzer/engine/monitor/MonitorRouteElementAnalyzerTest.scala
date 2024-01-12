package kpn.server.analyzer.engine.monitor

import kpn.core.util.UnitTest

class MonitorRouteElementAnalyzerTest extends UnitTest {

  test("continous") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2, 3)
        memberWay(11, "", 3, 4, 5)
        memberWay(12, "", 5, 6, 7)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>3>5>7"
        )
      )
    )
  }

  test("continous - first way reversed") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 3, 2, 1)
        memberWay(11, "", 3, 4, 5)
        memberWay(12, "", 5, 6, 7)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>3>5>7"
        )
      )
    )
  }

  test("continous - second and third way reversed") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2, 3)
        memberWay(11, "", 5, 4, 3)
        memberWay(12, "", 7, 6, 5)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>3>5>7"
        )
      )
    )
  }

  test("2 gaps - 3 segments") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2)
        memberWay(11, "", 3, 4)
        memberWay(12, "", 4, 5)
        memberWay(13, "", 6, 7)
        memberWay(14, "", 7, 8)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>2",
        ),
        Seq(
          "3>4>5",
        ),
        Seq(
          "6>7>8"
        )
      )
    )
  }

  test("start way has forward role") { // no backward path
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "forward", 1, 2)
        memberWay(11, "", 3, 2)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>2 (down)",
          "2>3"
        )
      )
    )
  }

  test("start way has forward role, no connection to second way") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(10, "forward", 2, 1)
        memberWay(11, "", 3, 2)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "2>1 (down)",
        ),
        Seq(
          "3>2"
        )
      )
    )
  }

  test("forward") {
    val result = analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2)
        memberWay(11, "forward", 2, 3)
        memberWay(12, "forward", 3, 8)
        memberWay(13, "forward", 8, 7)
        memberWay(14, "forward", 7, 2)
        memberWay(15, "", 8, 9)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>8 (down)",
          "8>7>2 (up)",
          "8>9",
        )
      )
    )
  }

  test("forward and gap") {
    val result = analyze(
      new MonitorRouteTestData() {
        memberWay(10, "", 1, 2)
        memberWay(11, "forward", 2, 3)
        memberWay(12, "forward", 3, 8)
        memberWay(13, "forward", 7, 2)
        memberWay(14, "forward", 8, 7)
        memberWay(15, "", 8, 9)
        memberWay(16, "", 10, 11)
        memberWay(17, "", 11, 12)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>8 (down)",
          "8>7>2 (up)",
          "8>9",
        ),
        Seq(
          "10>11>12",
        )
      )
    )
  }

  // TODO add test with first way 'forward'/'backward' role?

  private def analyze(data: MonitorRouteTestData): Seq[Seq[String]] = {
    val relation = data.relation
    val elementGroups = MonitorRouteElementAnalyzer.analyze(relation.members)
    elementGroups.map(_.elements.map(_.string))
  }
}
