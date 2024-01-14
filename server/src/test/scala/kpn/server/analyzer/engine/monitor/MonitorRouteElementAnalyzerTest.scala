package kpn.server.analyzer.engine.monitor

import kpn.core.util.UnitTest

class MonitorRouteElementAnalyzerTest extends UnitTest {

  test("continous") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(11, "", 1, 2, 3)
        memberWay(12, "", 3, 4, 5)
        memberWay(13, "", 5, 6, 7)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>3>5>7"
        )
      )
    )
  }

  test("gap after second way") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(11, "", 1, 2, 3)
        memberWay(12, "", 3, 4, 5)
        // gap
        memberWay(13, "", 6, 7, 8)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>3>5",
        ),
        Seq(
          "6>8",
        )
      )
    )
  }

  test("continous - first way reversed") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(11, "", 3, 2, 1)
        memberWay(12, "", 3, 4, 5)
        memberWay(13, "", 5, 6, 7)
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
        memberWay(11, "", 1, 2, 3)
        memberWay(12, "", 5, 4, 3)
        memberWay(13, "", 7, 6, 5)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>3>5>7"
        )
      )
    )
  }

  test("gap after first and third way") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(11, "", 1, 2)
        // gap
        memberWay(12, "", 3, 4)
        memberWay(13, "", 4, 5)
        // gap
        memberWay(14, "", 6, 7)
        memberWay(15, "", 7, 8)
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

  test("gap after second way and before last way") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(11, "", 1, 2)
        memberWay(12, "", 2, 3)
        // gap
        memberWay(13, "", 4, 5)
        memberWay(14, "", 5, 6)
        // gap
        memberWay(15, "", 7, 8)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>2>3",
        ),
        Seq(
          "4>5>6",
        ),
        Seq(
          "7>8"
        )
      )
    )
  }

  test("start way has forward role") { // no backward path
    analyze(
      new MonitorRouteTestData() {
        memberWay(11, "forward", 1, 2)
        memberWay(12, "", 3, 2)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>2 (Down)",
          "2>3"
        )
      )
    )
  }

  test("start way has forward role, no connection to second way") {
    analyze(
      new MonitorRouteTestData() {
        memberWay(11, "forward", 2, 1)
        memberWay(12, "", 3, 2)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "2>1 (Down)",
        ),
        Seq(
          "3>2"
        )
      )
    )
  }

  test("forward/backward") {
    val result = analyze(
      new MonitorRouteTestData() {
        memberWay(11, "", 1, 2)
        memberWay(12, "forward", 2, 3)
        memberWay(13, "forward", 3, 8)
        memberWay(14, "backward", 2, 7)
        memberWay(15, "backward", 7, 8)
        memberWay(16, "", 8, 9)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>8 (Down)",
          "8>7>2 (Up)",
          "8>9",
        )
      )
    )
  }

  test("forward/backward 2") { // TODO further investigate deviation from reference
    val result = analyze(
      new MonitorRouteTestData() {
        memberWay(11, "", 1, 2)
        memberWay(12, "forward", 2, 3)
        memberWay(13, "forward", 3, 8)
        memberWay(14, "backward", 2, 7)
        memberWay(15, "forward", 8, 7)
        memberWay(16, "", 8, 9)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>8 (Down)",
          "8>7>2 (Up)",
          "8>9",
        )
      )
    )
  }

  test("forward") { // TODO further investigate deviation from reference
    val result = analyze(
      new MonitorRouteTestData() {
        memberWay(11, "", 1, 2)
        memberWay(12, "forward", 2, 3)
        memberWay(13, "forward", 3, 8)
        memberWay(14, "forward", 7, 2)
        memberWay(15, "forward", 8, 7)
        memberWay(16, "", 8, 9)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>8 (Down)",
          "8>7>2 (Up)",
          "8>9",
        )
      )
    )
  }

  test("forward and gap") { // TODO further investigate deviation from reference
    val result = analyze(
      new MonitorRouteTestData() {
        memberWay(11, "", 1, 2)
        memberWay(12, "forward", 2, 3)
        memberWay(13, "forward", 3, 8)
        memberWay(14, "forward", 7, 2)
        memberWay(15, "forward", 8, 7)
        memberWay(16, "", 8, 9)
        memberWay(17, "", 10, 11)
        memberWay(18, "", 11, 12)
      }
    ).shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>8 (Down)",
          "8>7>2 (Up)",
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
    new ReferencePrinter().print(relation)
    val elementGroups = MonitorRouteElementAnalyzer.analyze(relation.members)
    elementGroups.map(_.elements.map(_.string))
  }
}
