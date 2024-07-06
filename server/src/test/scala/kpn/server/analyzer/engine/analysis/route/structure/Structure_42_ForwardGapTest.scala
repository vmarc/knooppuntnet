package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.core.util.UnitTest

class Structure_42_ForwardGapTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "forward", 2, 1)
    memberWay(12, "", 3, 2)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Seq(RouteNotForward, RouteNotBackward, RouteNotContinious, RouteBroken))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp ■   bp     head ■   tail     d forward",
        //
        "2    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 2>1",
        "  element-1 forward 2>1",
        "    way-11  p     n     loop     fp ■   bp     head ■   tail     d forward  paths=1",
        "segment-2 3>2",
        "  element-2 bidirectional 3>2",
        "    way-12  p     n     loop     fp     bp     head     tail     d unconnected  paths=2",
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
        TestPathNodes(1, Seq(2, 1)),
        TestPathNodes(2, Seq(3, 2)),
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "other=2>1 nodes=2, 1",
        "other=3>2 nodes=3, 2",
      )
    )
  }

  test("elements") {
    setup.elementGroups().shouldMatchTo(
      Seq(
        Seq(
          "2>1 (Forward)",
        ),
        Seq(
          "3>2"
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
          TestStructurePath(2, 1, Seq(2, 1)),
          TestStructurePath(3, 2, Seq(3, 2))
        )
      )
    )
  }
}
