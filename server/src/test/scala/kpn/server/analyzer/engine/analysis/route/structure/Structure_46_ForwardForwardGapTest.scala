package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.core.util.UnitTest

class Structure_46_ForwardForwardGapTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "forward", 3, 8)
    memberWay(14, "forward", 7, 2)
    memberWay(15, "forward", 8, 7)
    memberWay(16, "", 8, 9)
    //
    memberWay(17, "", 10, 11)
    memberWay(18, "", 11, 12)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(
      Set(RouteNotForward,
        RouteNotBackward,
        RouteNotContinious,
        RouteBroken
      )
    )
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "5    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "6    p ■   n     loop     fp     bp     head     tail     d forward",
        //
        "7    p     n ■   loop     fp     bp     head     tail     d forward",
        "8    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>9",
        "  element-1 bidirectional 1>2",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "  element-2 forward 2>8",
        "    way-12  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward  paths=2",
        "    way-13  p ■   n ■   loop     fp ■   bp     head     tail     d forward  paths=2",
        "  element-3 backward 2>8",
        "    way-14  p ■   n ■   loop     fp     bp ■   head     tail     d backward  paths=3",
        "    way-15  p ■   n ■   loop     fp     bp ■   head     tail ■   d backward  paths=3",
        "  element-4 bidirectional 8>9",
        "    way-16  p ■   n     loop     fp     bp     head     tail     d forward  paths=4",
        "segment-2 10>12",
        "  element-5 bidirectional 10>12",
        "    way-17  p     n ■   loop     fp     bp     head     tail     d forward  paths=5",
        "    way-18  p ■   n     loop     fp     bp     head     tail     d forward  paths=5",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1, bidirectional, elements=1",
        "path-2, forward, elements=2",
        "path-3, backward, elements=3",
        "path-4, bidirectional, elements=4",
        "path-5, bidirectional, elements=5",
      )
    )

    context.pathNodes.shouldMatchTo(
      Seq(
        TestPathNodes(1, Seq(1, 2)),
        TestPathNodes(2, Seq(2, 3, 8)),
        TestPathNodes(3, Seq(2, 7, 8)),
        TestPathNodes(4, Seq(8, 9)),
        TestPathNodes(5, Seq(10, 11, 12)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "other=1>2 nodes=1, 2",
        "other=2>8 nodes=2, 3, 8",
        "other=8>2 nodes=2, 7, 8",
        "other=8>9 nodes=8, 9",
        "other=10>12 nodes=10, 11, 12",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2",
          "2>3>8 (Forward)",
          "8>7>2 (Backward)",
          "8>9",
        ),
        Seq(
          "10>11>12",
        )
      )
    )
  }

  test("structure") {
    val structure = setup.structure()
    structure.shouldMatchTo(
      TestStructure(
        forwardPath = None,
        backwardPath = None,
        Seq(
          TestStructurePath(1, 9, Seq(1, 2, 2, 3, 8, 8, 7, 2, 8, 9)),
          TestStructurePath(10, 12, Seq(10, 11, 12))
        )
      )
    )
  }
}
