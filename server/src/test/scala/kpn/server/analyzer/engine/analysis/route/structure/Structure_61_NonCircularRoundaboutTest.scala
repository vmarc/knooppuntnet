package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_61_NonCircularRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberRoundabout(12, "", 3, 4, 5)
    memberWay(13, "", 5, 7, 8)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Seq.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d roundaboutright",
        "3    p ■   n     loop     fp     bp     head     tail     d forward"
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>8",
        "  element-1 bidirectional 1>8",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright  paths=1",
        "    way-13  p ■   n     loop     fp     bp     head     tail     d forward  paths=1",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Seq(1, 2, 3, 4, 5, 7, 8)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>8 nodes=1, 2, 3, 4, 5, 7, 8",
        "backward=8>1 nodes=8, 7, 5, 4, 3, 2, 1",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3",
          "3>5 (Forward)",
          "5>8",
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
            endNodeId = 8,
            nodeIds = Seq(1, 2, 3, 4, 5, 7, 8)
          )
        ),
        backwardPath = None
      )
    )
  }
}
