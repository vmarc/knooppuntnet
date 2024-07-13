package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.core.util.UnitTest

// broken in simple route
class Structure_N12_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(6, "02")
    memberNode(1)
    memberWay(10, "", 1, 2, 3) // broken
    memberWay(11, "", 4, 5)
    memberWay(12, "", 5, 6)
    memberNode(6)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()
    context.facts.shouldMatchTo(Set(RouteNotForward, RouteNotBackward, RouteNotContinious, RouteBroken))
    context.links.shouldMatchTo(
      Seq(
        "1    ",
        "2    p     n     loop     fp     bp     head     tail     d unconnected",
        "3    p     n ■   loop     fp     bp     head     tail     d forward",
        "4    p ■   n     loop     fp     bp     head     tail     d forward",
        "5    ",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=1(01)",
        "end=6(02)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>3",
        "  element-1 bidirectional 1>3  1(01)",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected  paths=1",
        "segment-2 4>6",
        "  element-2 bidirectional 4>6  6(02)",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward  paths=2",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d forward  paths=2",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1 ↔ elements=1, nodes=1, 2, 3",
        "path-2 ↔ elements=2, nodes=4, 5, 6",
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "other=1>3 nodes=1, 2, 3",
        "other=4>6 nodes=4, 5, 6",
      )
    )
  }
}
