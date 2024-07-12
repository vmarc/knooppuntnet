package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_62_NonCircularRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberRoundabout(12, "forward", 3, 4, 5)
    memberRoundabout(13, "forward", 5, 6, 3)
    memberWay(14, "", 5, 7, 8)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)

    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "3    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "4    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>8",
        "  element-1 bidirectional 1>3",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "  element-2 forward 3>5",
        "    way-12  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward  paths=2",
        "  element-3 backward 3>5",
        "    way-13  p ■   n ■   loop     fp     bp ■   head     tail ■   d backward  paths=3",
        "  element-4 bidirectional 5>8",
        "    way-14  p ■   n     loop     fp     bp     head     tail     d forward  paths=4",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
        "path-2, forward, elements=2",
        "path-3, backward, elements=3",
        "path-4, bidirectional, elements=4",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Vector(1, 2, 3)),
        TestPathNodes(2, Vector(3, 4, 5)),
        TestPathNodes(3, Vector(3, 6, 5)),
        TestPathNodes(4, Vector(5, 7, 8)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>8 nodes=1, 2, 3, 4, 5, 7, 8",
        "backward=8>1 nodes=8, 7, 5, 6, 3, 2, 1",
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
