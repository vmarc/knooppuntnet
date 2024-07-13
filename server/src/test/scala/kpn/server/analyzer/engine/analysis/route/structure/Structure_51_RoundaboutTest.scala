package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_51_RoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberRoundabout(11, "", 1, 2, 3, 4, 1)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d roundaboutright",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>1",
        "  element-1 bidirectional 1>1",
        "    way-11  p     n     loop     fp     bp     head     tail     d roundaboutright  paths=1",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1 ↔ elements=1, nodes=1, 2, 3, 4, 1",
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>1 nodes=1, 2, 3, 4, 1",
        "backward=1>1 nodes=1, 4, 3, 2, 1",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>1 (Forward)",
          "1>1 (Backward)",
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
            endNodeId = 1,
            nodeIds = Seq(1, 2, 3, 4, 1)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 1,
            endNodeId = 1,
            nodeIds = Seq(1, 2, 3, 4, 1)
          )
        )
      )
    )
  }
}
