package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteBroken
import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotContinious
import kpn.api.common.Fact.RouteNotForward
import kpn.core.util.UnitTest

class Structure_31_GapSecondWayBackwardTest extends UnitTest {

  // direction of first way derived from second way - second way backward - no connection
  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "backward", 3, 4, 5)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set(RouteNotForward, RouteNotBackward, RouteNotContinious, RouteBroken))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
        "2    p     n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  ↔  nodes=1, 2, 3",
        "    way-11  p     n     loop     fp     bp     head     tail     d unconnected",
        "segment-2 5>3",
        "  element-2 5>3  →  nodes=5, 4, 3",
        "    way-12  p     n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "other=1>3 nodes=1, 2, 3",
        "other=5>3 nodes=5, 4, 3",
      )
    )
  }
}
