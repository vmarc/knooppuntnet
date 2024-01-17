package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_11_DerivedDirectionTest extends UnitTest {

  // direction of first way derived from second way
  private def setup = new StructureTestSetup() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "", 3, 4, 5)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3>5"
        )
      )
    )
  }

  test("structure") {
    pending
    setup.structure()
  }
}
