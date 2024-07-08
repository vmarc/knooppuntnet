package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// reproduces situation in route 16828788 (EV1 La Tranche-sur-Mer — Marans)
class Structure_77_Split_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberRoundabout(12, "", 2, 3, 4, 5, 2)
    memberWay(13, "forward", 4, 6)
    memberWay(14, "forward", 6, 4)
    memberWay(15, "", 6, 7)
  }.build

  test("analyze") {
    val context = setup.analyze()
    // context.facts.shouldMatchTo(Seq.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d roundaboutright",
        "3    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "5    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))
    context.pathNodes.foreach(a => println(s"""$a,"""))
    context.pathDetails.foreach(a => println(s""""$a","""))

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>7",
        "  element-1 bidirectional 1>2",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright  paths=1",
        "  element-2 forward 4>6",
        "    way-13  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward  paths=2",
        "  element-3 backward 4>6",
        "    way-14  p ■   n ■   loop     fp     bp ■   head     tail ■   d backward  paths=3",
        "  element-4 bidirectional 6>7",
        "    way-15  p ■   n     loop     fp     bp     head     tail     d forward  paths=4",
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
        TestPathNodes(1, Vector(1, 2, 3, 4, 5, 2)),
        TestPathNodes(2, Vector(4, 6)),
        TestPathNodes(3, Vector(4, 6)),
        TestPathNodes(4, Vector(6, 7)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>7 nodes=1, 2, 3, 4, 6, 7",
        "backward=7>1 nodes=7, 6, 4, 5, 2, 1",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>4 (Forward)",
          "4>2 (Backward)",
          "4>6 (Forward)",
          "6>4 (Backward)",
          "6>7",
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
            endNodeId = 7,
            nodeIds = Seq(1, 2, 3, 4, 6, 7)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 7,
            endNodeId = 1,
            nodeIds = Seq(7, 6, 4, 5, 2, 1)
          )
        )
      )
    )
  }
}
