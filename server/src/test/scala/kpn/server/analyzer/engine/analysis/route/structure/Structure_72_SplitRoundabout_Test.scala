package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// reproduces situation in route 5444896 (EV1 Roscoff — Morlaix)
class Structure_72_SplitRoundabout_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    // before split:
    memberWay(11, "", 1, 2)
    // down until roundabout:
    memberWay(12, "forward", 2, 5)
    // up from roundabout:
    memberWay(13, "forward", 8, 2)
    memberWay(14, "forward", 4, 8)
    // roundabout:
    memberRoundabout(15, "forward", 3, 4, 5, 6, 7, 3)
    // down until roundabout:
    memberWay(16, "forward", 6, 9)
    memberWay(17, "forward", 9, 10)
    // up from roundabout
    memberWay(18, "forward", 11, 7)
    memberWay(19, "forward", 10, 11)
    // continue:
    memberWay(20, "", 10, 12)
  }.build

  test("analyze") {
    val context = setup.analyze()
    // context.facts.shouldMatchTo(Seq.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "3    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "5    p ■   n ■   loop     fp ■   bp     head     tail ■   d roundaboutright",
        "6    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "7    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "8    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "9    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "10    p ■   n     loop     fp     bp     head     tail     d forward"
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>12",
        "  element-1 bidirectional 1>2",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "  element-2 forward 2>5",
        "    way-12  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward  paths=2",
        "  element-3 backward 2>4",
        "    way-13  p ■   n ■   loop     fp     bp ■   head     tail     d backward  paths=3",
        "    way-14  p ■   n ■   loop     fp     bp ■   head     tail     d backward  paths=3",
        "  element-4 forward 5>6",
        "    way-15  p ■   n ■   loop     fp ■   bp     head     tail ■   d roundaboutright  paths=4",
        "  element-5 backward 4>7",
        "    way-15  p ■   n ■   loop     fp ■   bp     head     tail ■   d roundaboutright  paths=5",
        "  element-6 forward 6>10",
        "    way-16  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward  paths=6",
        "    way-17  p ■   n ■   loop     fp ■   bp     head     tail     d forward  paths=6",
        "  element-7 backward 7>10",
        "    way-18  p ■   n ■   loop     fp     bp ■   head     tail     d backward  paths=7",
        "    way-19  p ■   n ■   loop     fp     bp ■   head     tail ■   d backward  paths=7",
        "  element-8 bidirectional 10>12",
        "    way-20  p ■   n     loop     fp     bp     head     tail     d forward  paths=8",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
        "path-2, forward, elements=2",
        "path-3, backward, elements=3",
        "path-4, forward, elements=4",
        "path-5, backward, elements=5",
        "path-6, forward, elements=6",
        "path-7, backward, elements=7",
        "path-8, bidirectional, elements=8",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Vector(1, 2)),
        TestPathNodes(2, Vector(2, 5)),
        TestPathNodes(3, Vector(2, 8, 4)),
        TestPathNodes(4, Vector(5, 6)),
        TestPathNodes(5, Vector(4, 3, 7)),
        TestPathNodes(6, Vector(6, 9, 10)),
        TestPathNodes(7, Vector(7, 11, 10)),
        TestPathNodes(8, Vector(10, 12)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>12 nodes=1, 2, 5, 6, 9, 10, 12",
        "backward=12>1 nodes=12, 10, 11, 7, 3, 4, 8, 2, 1",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>5 (Forward)",
          "4>8>2 (Backward)",
          "5>6 (Forward)",
          "7>4 (Backward)",
          "6>9>10 (Forward)",
          "10>11>7 (Backward)",
          "10>12",
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
            endNodeId = 12,
            nodeIds = Seq(1, 2, 5, 6, 9, 10, 12)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 12,
            endNodeId = 1,
            nodeIds = Seq(12, 10, 11, 7, 3, 4, 8, 2, 1)
          )
        )
      )
    )
  }
}
