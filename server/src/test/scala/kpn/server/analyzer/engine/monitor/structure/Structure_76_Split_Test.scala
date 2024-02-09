package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

// reproduces situation in route 16842517 (EV1 Mimizan Plage — Léon)
class Structure_76_Split_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "backward", 2, 1)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "forward", 4, 1)
    memberWay(14, "forward", 3, 4)
  }.build

  test("reference") {
    setup.reference(traceEnabled = true).shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp ■   bp     head ■   tail     d backward",
        "2    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "4    p ■   n     loop     fp     bp ■   head     tail ■   d backward",
      )
    )
  }

  test("elements") {
    setup.elementGroups(traceEnabled = true).shouldMatchTo(
      Seq(
        Seq(
          "1>2>3 (Down)",
          "3>4>1 (Up)",
        ),
      )
    )
  }

  test("structure") {
    val structure = setup.structure()
    structure.shouldMatchTo(
      TestStructure(
        forwardPath = Some(
          TestStructurePath(
            startNodeId = 1,
            endNodeId = 3,
            nodeIds = Seq(1, 2, 3)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 3,
            endNodeId = 1,
            nodeIds = Seq(3, 4, 1)
          )
        )
      )
    )
  }
}
