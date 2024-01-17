package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_34_GapTest extends UnitTest {

  private def setup = new StructureTestSetup() {
    memberWay(11, "", 1, 2)
    memberWay(12, "", 2, 3)
    // gap
    memberWay(13, "", 4, 5)
    memberWay(14, "", 5, 6)
    // gap
    memberWay(15, "", 7, 8)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d forward",
        //
        "3    p     n ■   loop     fp     bp     head     tail     d forward",
        "4    p ■   n     loop     fp     bp     head     tail     d forward",
        //
        "5    p     n     loop     fp     bp     head     tail     d none",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
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

  test("structure") {
    pending
    setup.structure()
  }
}
