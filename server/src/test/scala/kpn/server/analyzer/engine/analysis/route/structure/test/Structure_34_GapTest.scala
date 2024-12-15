package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteBroken
import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotContinious
import kpn.api.common.Fact.RouteNotForward
import kpn.core.util.UnitTest

class Structure_34_GapTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberWay(12, "", 2, 3)
    // gap
    memberWay(13, "", 4, 5)
    memberWay(14, "", 5, 6)
    // gap
    memberWay(15, "", 7, 8)
  }.build

  test("analyze") {
    val context = setup.analyze()

    assertEqual(
      context.facts,
      Set(RouteNotForward, RouteNotBackward, RouteNotContinious, RouteBroken)
    )

    assertEqual(
      context.links,
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d forward",
        //
        "3    p     n ■   loop     fp     bp     head     tail     d forward",
        "4    p ■   n     loop     fp     bp     head     tail     d forward",
        //
        "5    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  ↔  nodes=1, 2, 3",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d forward",
        "segment-2 4>6",
        "  element-2 4>6  ↔  nodes=4, 5, 6",
        "    way-13  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-14  p ■   n     loop     fp     bp     head     tail     d forward",
        "segment-3 7>8",
        "  element-3 7>8  ↔  nodes=7, 8",
        "    way-15  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "other=1>3 nodes=1, 2, 3",
        "other=4>6 nodes=4, 5, 6",
        "other=7>8 nodes=7, 8",
      )
    )
  }
}
