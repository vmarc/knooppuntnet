package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_03_SingleWayBackwardTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "backward", 1, 2, 3)
  }.build

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
          "3>1 (Forward)"
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
            startNodeId = 3,
            endNodeId = 1,
            nodeIds = Seq(3, 2, 1)
          )
        ),
        backwardPath = None
      )
    )
  }
}
