package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_41_ForwardNoBackwardPathTest extends UnitTest {

  private def setup = new StructureTestSetup() {
    memberWay(11, "forward", 1, 2)
    memberWay(12, "", 3, 2)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp ■   bp     head ■   tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d backward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2 (Down)",
          "2>3"
        )
      )
    )
  }

  test("structure") {
    pending
    setup.structure()
  }
}
