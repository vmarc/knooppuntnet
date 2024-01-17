package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_33_GapTest extends UnitTest {

  private def setup = new StructureTestSetup() {
    memberWay(11, "", 1, 2)
    // gap
    memberWay(12, "", 3, 4)
    memberWay(13, "", 4, 5)
    // gap
    memberWay(14, "", 6, 7)
    memberWay(15, "", 7, 8)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d none",
        //
        "2    p     n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n     loop     fp     bp     head     tail     d forward",
        //
        "4    p     n ■   loop     fp     bp     head     tail     d forward",
        "5    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
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

  test("structure") {
    pending
    setup.structure()
  }
}
