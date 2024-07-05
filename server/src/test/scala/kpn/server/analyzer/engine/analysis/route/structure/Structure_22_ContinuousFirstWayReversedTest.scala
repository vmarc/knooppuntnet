package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_22_ContinuousFirstWayReversedTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 3, 2, 1)
    memberWay(12, "", 3, 4, 5)
    memberWay(13, "", 5, 6, 7)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Seq.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d backward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>7",
        "  element-1 bidirectional 1>7",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d backward  paths=1",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=1",
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
        TestPathNodes(1, Seq(1, 2, 3, 4, 5, 6, 7)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>7 nodes=1, 2, 3, 4, 5, 6, 7",
        "backward=7>1 nodes=7, 6, 5, 4, 3, 2, 1",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>3>5>7"
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
            endNodeId = 7,
            nodeIds = Seq(1, 2, 3, 4, 5, 6, 7)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 7,
            endNodeId = 1,
            nodeIds = Seq(7, 6, 5, 4, 3, 2, 1)
          )
        )
      )
    )
  }
}
