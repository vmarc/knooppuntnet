package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_14_DerivedDirectionBackwardTest extends UnitTest {

  // direction of first way derived from second way - second way backward
  private def setup = new StructureTestSetup() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "backward", 5, 4, 3)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3",
          "3>5 (Down)",
        )
      )
    )
  }

  test("structure") {
    pending
    setup.structure()
  }
}
