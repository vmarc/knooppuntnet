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
        "  element-1 bidirectional 1>3",
        "    way-11  p     n ■   loop ■   fp     bp     head     tail     d roundaboutright  paths=1",
        "    way-12  p ■   n     loop ■   fp     bp     head     tail     d roundaboutright  paths=1",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
      )
    )

    pending
    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Seq(1, 2, 3, 4, 1, 5, 6, 7, 3)), // TODO redesign - this is NOK?
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>3 nodes=1, 2, 3, 4, 1, 5, 6, 7, 3", // TODO redesign - this is NOK?
        "backward=3>1 nodes=3, 7, 6, 5, 1, 4, 3, 2, 1", // TODO redesign - this is NOK?
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
