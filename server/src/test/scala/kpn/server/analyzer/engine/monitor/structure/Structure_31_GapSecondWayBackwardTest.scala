package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_31_GapSecondWayBackwardTest extends UnitTest {

  // direction of first way derived from second way - second way backward - no connection
  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "backward", 3, 4, 5)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d none",
        "2    p     n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3",
        ),
        Seq(
          "5>3 (Up)"
        )
      )
    )
  }

  test("structure") {
    pending
    setup.structure()
  }
}
