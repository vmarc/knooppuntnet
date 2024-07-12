package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// reproduces situation in route 5444896 (EV1 Roscoff — Morlaix)
class Structure_71_5444896_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 2, 1) // 1, 2
    // down:
    memberWay(12, "backward", 3, 2) // 2, 3
    memberWay(13, "forward", 3, 4) // 3, 4
    memberWay(14, "backward", 5, 4) // 4, 5
    memberWay(15, "backward", 8, 5) // 5, 8
    // up:
    memberWay(16, "forward", 6, 2) // 6, 2
    memberWay(17, "forward", 7, 6) // 7, 6
    memberWay(18, "backward", 7, 8) // 8, 7
    // continue:
    memberWay(19, "", 9, 8)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d backward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d backward",
        "3    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp ■   bp     head     tail     d backward",
        "5    p ■   n ■   loop     fp ■   bp     head     tail     d backward",
        "6    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "7    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "8    p ■   n ■   loop     fp     bp ■   head     tail ■   d forward",
        "9    p ■   n     loop     fp     bp     head     tail     d backward"
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>9",
        "  element-1 bidirectional 1>2",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d backward  paths=1",
        "  element-2 forward 2>8",
        "    way-12  p ■   n ■   loop     fp ■   bp     head ■   tail     d backward  paths=2",
        "    way-13  p ■   n ■   loop     fp ■   bp     head     tail     d forward  paths=2",
        "    way-14  p ■   n ■   loop     fp ■   bp     head     tail     d backward  paths=2",
        "    way-15  p ■   n ■   loop     fp ■   bp     head     tail     d backward  paths=2",
        "  element-3 backward 2>8",
        "    way-16  p ■   n ■   loop     fp     bp ■   head     tail     d backward  paths=3",
        "    way-17  p ■   n ■   loop     fp     bp ■   head     tail     d backward  paths=3",
        "    way-18  p ■   n ■   loop     fp     bp ■   head     tail ■   d forward  paths=3",
        "  element-4 bidirectional 8>9",
        "    way-19  p ■   n     loop     fp     bp     head     tail     d backward  paths=4",
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
        TestPathNodes(1, Seq(1, 2)),
        TestPathNodes(2, Seq(2, 3, 4, 5, 8)),
        TestPathNodes(3, Seq(2, 6, 7, 8)),
        TestPathNodes(4, Seq(8, 9)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>9 nodes=1, 2, 3, 4, 5, 8, 9",
        "backward=9>1 nodes=9, 8, 7, 6, 2, 1",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>4>5>8 (Forward)",
          "8>7>6>2 (Backward)",
          "8>9",
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
            endNodeId = 9,
            nodeIds = Seq(1, 2, 3, 4, 5, 8, 9)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 9,
            endNodeId = 1,
            nodeIds = Seq(9, 8, 7, 6, 2, 1)
          )
        )
      )
    )
  }
}
