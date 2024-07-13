package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_55_DoubleRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberRoundabout(12, "", 3, 4, 5, 6, 3)
    memberRoundabout(13, "", 5, 7, 9, 8, 5)
    memberWay(14, "", 9, 10, 11)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d roundaboutright",
        "3    p ■   n ■   loop     fp     bp     head     tail     d roundaboutright",
        "4    p ■   n     loop     fp     bp     head     tail     d forward"
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>11",
        "  element-1 bidirectional 1>3",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "  element-2 forward 3>5",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright  paths=2",
        "  element-3 backward 3>5",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright  paths=3",
        "  element-4 forward 5>9",
        "    way-13  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright  paths=4",
        "  element-5 backward 5>9",
        "    way-13  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright  paths=5",
        "  element-6 bidirectional 9>11",
        "    way-14  p ■   n     loop     fp     bp     head     tail     d forward  paths=6",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1 ↔ elements=1, nodes=1, 2, 3",
        "path-2 → elements=2, nodes=3, 4, 5",
        "path-3 ← elements=3, nodes=5, 6, 3",
        "path-4 → elements=4, nodes=5, 7, 9",
        "path-5 ← elements=5, nodes=9, 8, 5",
        "path-6 ↔ elements=6, nodes=9, 10, 11",
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>11 nodes=1, 2, 3, 4, 5, 7, 9, 10, 11",
        "backward=11>1 nodes=11, 10, 9, 8, 5, 6, 3, 2, 1",
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
