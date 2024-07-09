package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_52_RoundaboutRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberRoundabout(11, "", 1, 2, 3, 4, 1)
    memberRoundabout(12, "", 3, 5, 6, 7, 3)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Seq.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop ■   fp     bp     head     tail     d roundaboutright",
        "2    p ■   n     loop ■   fp     bp     head     tail     d roundaboutright"
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>3",
        "  element-1 forward 1>3",
        "    way-11  p     n ■   loop ■   fp     bp     head     tail     d roundaboutright  paths=1",
        "  element-2 backward 1>3",
        "    way-11  p     n ■   loop ■   fp     bp     head     tail     d roundaboutright  paths=2",
        "  element-3 bidirectional 3>3",
        "    way-12  p ■   n     loop ■   fp     bp     head     tail     d roundaboutright  paths=3",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, forward, elements=1",
        "path-2, backward, elements=2",
        "path-3, bidirectional, elements=3",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Vector(1, 2, 3)),
        TestPathNodes(2, Vector(1, 4, 3)),
        TestPathNodes(3, Vector(3, 5, 6, 7, 3)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>3 nodes=1, 2, 3, 5, 6, 7, 3",
        "backward=3>1 nodes=3, 7, 6, 5, 3, 4, 1",
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
