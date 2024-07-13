package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// reproduces situation in route 16786092 (EV1 Morlaix — Carhaix-Plouguer)
class Structure_75_Split_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "forward", 1, 2)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "forward", 3, 4)
    memberWay(14, "backward", 1, 5)
    memberWay(15, "backward", 5, 6)
    memberWay(16, "backward", 6, 4)
    memberWay(17, "", 4, 7)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp ■   bp     head ■   tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "5    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "6    p ■   n ■   loop     fp     bp ■   head     tail ■   d forward",
        "7    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>7",
        "  element-1 forward 1>4",
        "    way-11  p     n ■   loop     fp ■   bp     head ■   tail     d forward  paths=1",
        "    way-12  p ■   n ■   loop     fp ■   bp     head     tail     d forward  paths=1",
        "    way-13  p ■   n ■   loop     fp ■   bp     head     tail     d forward  paths=1",
        "  element-2 backward 1>4",
        "    way-14  p ■   n ■   loop     fp     bp ■   head     tail     d forward  paths=2",
        "    way-15  p ■   n ■   loop     fp     bp ■   head     tail     d forward  paths=2",
        "    way-16  p ■   n ■   loop     fp     bp ■   head     tail ■   d forward  paths=2",
        "  element-3 bidirectional 4>7",
        "    way-17  p ■   n     loop     fp     bp     head     tail     d forward  paths=3",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1 → elements=1, nodes=1, 2, 3, 4",
        "path-2 ← elements=2, nodes=4, 6, 5, 1",
        "path-3 ↔ elements=3, nodes=4, 7",
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>7 nodes=1, 2, 3, 4, 7",
        "backward=7>1 nodes=7, 4, 6, 5, 1",
      )
    )
  }

  test("elements") {
    setup.elementGroups(traceEnabled = true).shouldMatchTo(
      Seq(
        Seq(
          "1>2>3>4 (Forward)",
          "4>6>5>1 (Backward)",
          "4>7",
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
            nodeIds = Seq(1, 2, 3, 4, 7)
          )
        ),
        backwardPath = Some(
          TestStructurePath(
            startNodeId = 7,
            endNodeId = 1,
            nodeIds = Seq(7, 4, 6, 5, 1)
          )
        )
      )
    )
  }
}
