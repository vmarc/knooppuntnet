package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.core.util.UnitTest

class Structure_33_GapTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    // gap
    memberWay(12, "", 3, 4)
    memberWay(13, "", 4, 5)
    // gap
    memberWay(14, "", 6, 7)
    memberWay(15, "", 7, 8)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set(RouteNotForward, RouteNotBackward, RouteNotContinious, RouteBroken))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
        //
        "2    p     n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n     loop     fp     bp     head     tail     d forward",
        //
        "4    p     n ■   loop     fp     bp     head     tail     d forward",
        "5    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>2",
        "  element-1 bidirectional 1>2",
        "    way-11  p     n     loop     fp     bp     head     tail     d unconnected  paths=1",
        "segment-2 3>5",
        "  element-2 bidirectional 3>5",
        "    way-12  p     n ■   loop     fp     bp     head     tail     d forward  paths=2",
        "    way-13  p ■   n     loop     fp     bp     head     tail     d forward  paths=2",
        "segment-3 6>8",
        "  element-3 bidirectional 6>8",
        "    way-14  p     n ■   loop     fp     bp     head     tail     d forward  paths=3",
        "    way-15  p ■   n     loop     fp     bp     head     tail     d forward  paths=3",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1 ↔ elements=1, nodes=1, 2",
        "path-2 ↔ elements=2, nodes=3, 4, 5",
        "path-3 ↔ elements=3, nodes=6, 7, 8",
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "other=1>2 nodes=1, 2",
        "other=3>5 nodes=3, 4, 5",
        "other=6>8 nodes=6, 7, 8",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "1>2",
        ),
        Seq(
          "3>4>5",
        ),
        Seq(
          "6>7>8"
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
          TestStructurePath(1, 2, Seq(1, 2)),
          TestStructurePath(3, 5, Seq(3, 4, 5)),
          TestStructurePath(6, 8, Seq(6, 7, 8))
        )
      )
    )
  }
}
