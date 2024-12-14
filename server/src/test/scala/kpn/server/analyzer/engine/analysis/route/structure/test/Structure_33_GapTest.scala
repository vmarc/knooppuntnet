package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteBroken
import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotContinious
import kpn.api.common.Fact.RouteNotForward
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

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
        "  element-1 1>2  ↔  nodes=1, 2",
        "    way-11  p     n     loop     fp     bp     head     tail     d unconnected",
        "segment-2 3>5",
        "  element-2 3>5  ↔  nodes=3, 4, 5",
        "    way-12  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-13  p ■   n     loop     fp     bp     head     tail     d forward",
        "segment-3 6>8",
        "  element-3 6>8  ↔  nodes=6, 7, 8",
        "    way-14  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-15  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "other=1>2 nodes=1, 2",
        "other=3>5 nodes=3, 4, 5",
        "other=6>8 nodes=6, 7, 8",
      )
    )
  }
}
