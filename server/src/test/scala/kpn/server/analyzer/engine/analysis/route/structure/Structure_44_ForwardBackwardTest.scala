package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_44_ForwardBackwardTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "forward", 3, 5)
    memberWay(14, "backward", 2, 4)
    memberWay(15, "forward", 5, 4)
    memberWay(16, "", 5, 6)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "5    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "6    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>6",
        "  element-1 bidirectional 1>2",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "  element-2 forward 2>5",
        "    way-12  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward  paths=2",
        "    way-13  p ■   n ■   loop     fp ■   bp     head     tail     d forward  paths=2",
        "  element-3 backward 2>5",
        "    way-14  p ■   n ■   loop     fp     bp ■   head     tail     d forward  paths=3",
        "    way-15  p ■   n ■   loop     fp     bp ■   head     tail ■   d backward  paths=3",
        "  element-4 bidirectional 5>6",
        "    way-16  p ■   n     loop     fp     bp     head     tail     d forward  paths=4",
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
        TestPathNodes(2, Seq(2, 3, 5)),
        TestPathNodes(3, Seq(2, 4, 5)),
        TestPathNodes(4, Seq(5, 6)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>6 nodes=1, 2, 3, 5, 6",
        "backward=6>1 nodes=6, 5, 4, 2, 1",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>5 (Forward)",
          "5>4>2 (Backward)",
          "5>6"
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
            endNodeId = 6,
            nodeIds = Seq(1, 2, 3, 5, 6)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 6,
            endNodeId = 1,
            nodeIds = Seq(6, 5, 4, 2, 1)
          )
        )
      )
    )
  }
}
