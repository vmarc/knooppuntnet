package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_22_ContinuousSecondAndThirdWayReversedTest extends UnitTest {

  private def setup = new StructureTestSetup() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "", 5, 4, 3)
    memberWay(13, "", 7, 6, 5)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d backward",
        "3    p ■   n     loop     fp     bp     head     tail     d backward",
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
