package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_41_ForwardNoBackwardPathTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "forward", 1, 2)
    memberWay(12, "", 3, 2)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Seq.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp ■   bp     head ■   tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d backward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>3",
        "  element-1 forward 1>2",
        "    way-11  p     n ■   loop     fp ■   bp     head ■   tail     d forward  paths=1",
        "  element-2 bidirectional 2>3",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d backward  paths=2",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, forward, elements=1",
        "path-2, bidirectional, elements=2",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Seq(1, 2)),
        TestPathNodes(2, Seq(2, 3)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>3 nodes=1, 2, 3",
        "backward=3>2 nodes=3, 2",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2 (Forward)",
          "2>3"
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
            endNodeId = 3,
            nodeIds = Seq(1, 2, 3)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 3,
            endNodeId = 2,
            nodeIds = Seq(3, 2)
          )
        )
      )
    )
  }
}
