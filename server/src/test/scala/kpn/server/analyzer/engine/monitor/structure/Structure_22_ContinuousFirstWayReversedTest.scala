package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_22_ContinuousFirstWayReversedTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 3, 2, 1)
    memberWay(12, "", 3, 4, 5)
    memberWay(13, "", 5, 6, 7)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d backward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3>5>7"
        )
      )
    )
  }

  test("structure") {
    pending
    setup.structure()
  }
}
