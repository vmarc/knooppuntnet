package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// reproduces situation in route 16828788 (EV1 La Tranche-sur-Mer — Marans)
class Structure_77_Split_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberRoundabout(12, "", 2, 3, 4, 5, 2)
    memberWay(13, "forward", 4, 6)
    memberWay(14, "forward", 6, 4)
    memberWay(15, "", 6, 7)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d roundaboutright",
        "3    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "5    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>7",
        "  element-1 1>2  ↔  nodes=1, 2",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 2>4  →  nodes=2, 3, 4",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright",
        "  element-3 2>4  ←  nodes=2, 5, 4",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d roundaboutright",
        "  element-4 4>6  →  nodes=4, 6",
        "    way-13  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "  element-5 4>6  ←  nodes=4, 6",
        "    way-14  p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "  element-6 6>7  ↔  nodes=6, 7",
        "    way-15  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>7 nodes=1, 2, 3, 4, 6, 7",
        "backward=7>1 nodes=7, 6, 4, 5, 2, 1",
      )
    )
  }
}
