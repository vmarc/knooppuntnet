package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_32_GapTest extends UnitTest {

  private def setup = new StructureTestSetup() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "", 3, 4, 5)
    // gap
    memberWay(13, "", 6, 7, 8)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d forward",
        "3    p     n     loop     fp     bp     head     tail     d none",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
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

  test("structure") {
    pending
    setup.structure()
  }
}
