package kpn.server.analyzer.engine.monitor.structure

import kpn.core.util.UnitTest

class Structure_55_DoubleRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberRoundabout(12, "", 3, 4, 5, 6, 3)
    memberRoundabout(13, "", 5, 7, 9, 8, 5)
    memberWay(14, "", 9, 10, 11)
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
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3",
          "3>5 (Forward)",
          "5>3 (Backward)",
          "5>9 (Forward)",
          "9>5 (Backward)",
          "9>11",
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
            endNodeId = 11,
            nodeIds = Seq(1, 2, 3, 4, 5, 7, 9, 10, 11)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 11,
            endNodeId = 1,
            nodeIds = Seq(11, 10, 9, 8, 5, 6, 3, 2, 1)
          )
        )
      )
    )
  }
}
