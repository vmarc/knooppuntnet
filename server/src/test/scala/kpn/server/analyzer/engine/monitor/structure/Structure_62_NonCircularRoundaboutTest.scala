package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_62_NonCircularRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberRoundabout(12, "", 3, 4, 5)
    memberRoundabout(13, "", 5, 6, 3)
    memberWay(14, "", 5, 7, 8)
  }.build

  test("reference") {
    setup.reference().shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d roundabout_right",
        "3    p ■   n ■   loop     fp     bp     head     tail     d roundabout_right",
        "4    p ■   n     loop     fp     bp     head     tail     d forward"
      )
    )
  }

  test("elements") {
    pending
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3",
          "3>5 (Forward)",
          "5>3 (Backward)",
          "5>8",
        ),
      )
    )
  }

  test("structure") {
    pending
    val structure = setup.structure()
    structure.shouldMatchTo(
      TestStructure(
        forwardPath = Some(
          TestStructurePath(
            startNodeId = 1,
            endNodeId = 8,
            nodeIds = Seq(1, 2, 3, 4, 5, 7, 8)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 8,
            endNodeId = 1,
            nodeIds = Seq(8, 7, 5, 6, 3, 2, 1)
          )
        )
      )
    )
  }
}
