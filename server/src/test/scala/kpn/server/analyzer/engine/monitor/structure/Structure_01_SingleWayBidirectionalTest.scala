package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_01_SingleWayBidirectionalTest extends UnitTest {

  private def setup = new StructureTestSetup() {
    memberWay(11, "", 1, 2, 3)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d none",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3"
        )
      )
    )
  }

  test("structure") {
    pending
    setup.structure()
  }
}
