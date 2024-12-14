package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteBroken
import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotContinious
import kpn.api.common.Fact.RouteNotForward
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

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
        "  element-1 1>2  ↔  nodes=1, 2",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 2>8  →  nodes=2, 3, 8",
        "    way-12  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "    way-13  p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "  element-3 2>8  ←  nodes=2, 7, 8",
        "    way-14  p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "    way-15  p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "  element-4 8>9  ↔  nodes=8, 9",
        "    way-16  p ■   n     loop     fp     bp     head     tail     d forward",
        "segment-2 10>12",
        "  element-5 10>12  ↔  nodes=10, 11, 12",
        "    way-17  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-18  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "other=1>2 nodes=1, 2",
        "other=2>8 nodes=2, 3, 8",
        "other=2>8 nodes=2, 7, 8",
        "other=8>9 nodes=8, 9",
        "other=10>12 nodes=10, 11, 12",
      )
    )
  }
}
