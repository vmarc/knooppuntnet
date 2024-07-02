package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_05_SingleWayRoundaboutNotALoopTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberRoundabout(11, "", 1, 2, 3, 4)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d roundaboutright",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>4 (Forward)",
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
            endNodeId = 4,
            nodeIds = Seq(1, 2, 3, 4)
          )
        ),
        backwardPath = None
      )
    )
  }
}
