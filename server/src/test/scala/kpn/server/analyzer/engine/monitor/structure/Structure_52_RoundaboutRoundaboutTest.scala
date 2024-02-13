package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_52_RoundaboutRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberRoundabout(11, "", 1, 2, 3, 4, 1)
    memberRoundabout(12, "", 3, 5, 6, 7, 3)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop ■   fp     bp     head     tail     d roundabout_right",
        "2    p ■   n     loop ■   fp     bp     head     tail     d roundabout_right"
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3 (Forward)",
          "3>1 (Backward)",
          "3>3 (Forward)",
          "3>3 (Backward)",
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
            nodeIds = Seq(1, 2, 3, 5, 6, 7, 3)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 3,
            endNodeId = 1,
            nodeIds = Seq(3, 5, 6, 7, 3, 4, 1)
          )
        )
      )
    )
  }
}
