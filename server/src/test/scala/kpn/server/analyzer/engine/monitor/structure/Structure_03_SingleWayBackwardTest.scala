package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_03_SingleWayBackwardTest extends UnitTest {

  private def setup = new StructureTestSetup() {
    memberWay(11, "backward", 1, 2, 3)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "3>1 (Up)"
        )
      )
    )
  }

  test("structure") {
    pending
    setup.structure()
  }
}
