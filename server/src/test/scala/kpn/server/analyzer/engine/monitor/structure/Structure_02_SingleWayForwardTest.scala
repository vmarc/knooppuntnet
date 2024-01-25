package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_02_SingleWayForwardTest extends UnitTest {

  private def setup = new StructureTestSetup() {
    memberWay(11, "forward", 1, 2, 3)
  }

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n     loop     fp ■   bp     head ■   tail     d forward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3 (Down)"
        )
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
        backwardPath = None
      )
    )
  }
}
