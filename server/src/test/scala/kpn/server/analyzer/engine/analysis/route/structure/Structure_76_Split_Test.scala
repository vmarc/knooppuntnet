package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// reproduces situation in route 16842517 (EV1 Mimizan Plage — Léon)
class Structure_76_Split_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "backward", 2, 1)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "forward", 4, 1)
    memberWay(14, "forward", 3, 4)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Seq.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp ■   bp     head ■   tail     d backward",
        "2    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "4    p ■   n     loop     fp     bp ■   head     tail ■   d backward",
      )
    )

    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))
    context.pathNodes.foreach(a => println(s"""$a,"""))
    context.pathDetails.foreach(a => println(s""""$a","""))

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>3",
        "  element-1 forward 1>3",
        "    way-11  p     n ■   loop     fp ■   bp     head ■   tail     d backward  paths=1",
        "    way-12  p ■   n ■   loop     fp ■   bp     head     tail     d forward  paths=1",
        "  element-2 backward 1>3",
        "    way-13  p ■   n ■   loop     fp     bp ■   head     tail     d backward  paths=2",
        "    way-14  p ■   n     loop     fp     bp ■   head     tail ■   d backward  paths=2",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, forward, elements=1",
        "path-2, backward, elements=2",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Vector(1, 2, 3)),
        TestPathNodes(2, Vector(1, 4, 3)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>3 nodes=1, 2, 3",
        "backward=3>1 nodes=3, 4, 1",
      )
    )
  }

  test("elements") {
    setup.elementGroups(traceEnabled = true).shouldMatchTo(
      Seq(
        Seq(
          "1>2>3 (Forward)",
          "3>4>1 (Backward)",
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
            nodeIds = Seq(1, 2, 3)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 3,
            endNodeId = 1,
            nodeIds = Seq(3, 4, 1)
          )
        )
      )
    )
  }
}
