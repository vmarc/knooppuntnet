package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

// broken in way that 'overshoots'
class Structure_N13_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(6, "02")
    memberWay(10, "", 1, 2)
    memberWay(11, "", 2, 3, 4, 5) // overshoot
    memberWay(12, "", 3, 6)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()
    context.facts.shouldMatchTo(Set(RouteNotForward, RouteNotBackward, RouteNotContinious, RouteBroken))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d forward",
        "3    p     n     loop     fp     bp     head     tail     d unconnected",
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
        "segment-1 1>5",
        "  element-1 1>5  1(01)  ↔  nodes=1, 2, 3, 4, 5",
        "    way-10  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-11  p ■   n     loop     fp     bp     head     tail     d forward",
        "segment-2 3>6",
        "  element-2 3>6  6(02)  ↔  nodes=3, 6",
        "    way-12  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "other=1>5 nodes=1, 2, 3, 4, 5",
        "other=3>6 nodes=3, 6",
      )
    )
  }
}
