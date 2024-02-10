package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_15_DerivedDirectionBackwardBackwardTest extends UnitTest {

  // direction of first way derived from second way - second way backward - first way backward
  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 3, 2, 1)
    memberWay(12, "backward", 5, 4, 3)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d backward",
        "2    p ■   n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3",
          "3>5 (Forward)",
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
            endNodeId = 5,
            nodeIds = Seq(1, 2, 3, 4, 5)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 3,
            endNodeId = 1,
            nodeIds = Seq(3, 2, 1)
          )
        ),
      )
    )
  }
}
