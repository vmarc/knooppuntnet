package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_45_ForwardForwardTest extends UnitTest {

  private def setup = new StructureTestSetup() {
    memberWay(11, "", 1, 2)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "forward", 3, 8)
    memberWay(14, "forward", 7, 2)
    memberWay(15, "forward", 8, 7)
    memberWay(16, "", 8, 9)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "5    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "6    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
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

  test("structure") {
    pending
    setup.structure()
  }
}
