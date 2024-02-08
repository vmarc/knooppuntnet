package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

// reproduces situation in route 16786092 (EV1 Morlaix — Carhaix-Plouguer)
class Structure_74_Split_Test extends UnitTest {

  // TODO change setup? idea is to start with backward path
  private def setup = new StructureTestSetupBuilder() {
    memberWay(14, "backward", 1, 4)
    memberWay(15, "backward", 4, 3)
    memberWay(11, "forward", 1, 2)
    memberWay(12, "forward", 2, 3)
  }.build

  test("reference") {
    setup.reference(traceEnabled = true).shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp ■   bp     head ■   tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "4    p ■   n     loop     fp     bp ■   head     tail ■   d forward",
      )
    )
  }

  test("elements") {
    setup.elementGroups(traceEnabled = true).shouldMatchTo(
      Seq(
        Seq(
          "1>3 (Down)",
          "3>1 (Up)",
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
