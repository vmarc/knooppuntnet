package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteBroken
import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotForward
import kpn.core.util.UnitTest

class Structure_32_GapTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "", 3, 4, 5)
    // gap
    memberWay(13, "", 6, 7, 8)
  }.build

  test("analyze") {
    val context = setup.analyze()

    assertEqual(
      context.facts,
      Set(RouteNotForward, RouteNotBackward, RouteBroken)
    )

    assertEqual(
      context.links,
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d forward",
        "3    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>5",
        "  element-1 1>5  ↔  nodes=1, 2, 3, 4, 5",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d forward",
        "segment-2 6>8",
        "  element-2 6>8  ↔  nodes=6, 7, 8",
        "    way-13  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "other=1>5 nodes=1, 2, 3, 4, 5",
        "other=6>8 nodes=6, 7, 8",
      )
    )
  }
}
