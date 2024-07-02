package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_31_GapSecondWayBackwardTest extends UnitTest {

  // direction of first way derived from second way - second way backward - no connection
  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "backward", 3, 4, 5)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
        "2    p     n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3",
          "5>3 (Backward)"
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
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 5,
            endNodeId = 1,
            nodeIds = Seq(5, 4, 3, 2, 1)
          )
        )
      )
    )
  }
}
