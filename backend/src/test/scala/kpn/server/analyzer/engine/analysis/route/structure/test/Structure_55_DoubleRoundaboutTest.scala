package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest

class Structure_55_DoubleRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberRoundabout(12, "", 3, 4, 5, 6, 3)
    memberRoundabout(13, "", 5, 7, 9, 8, 5)
    memberWay(14, "", 9, 10, 11)
  }.build

  test("analyze") {
    val context = setup.analyze()

    assertEqual(
      context.facts,
      Set.empty
    )

    assertEqual(
      context.links,
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d roundabout-right",
        "3    p ■   n ■   loop     fp     bp     head     tail     d roundabout-right",
        "4    p ■   n     loop     fp     bp     head     tail     d forward"
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>11",
        "  element-1 1>3  ↔  nodes=1, 2, 3",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 3>5  →  nodes=3, 4, 5",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d roundabout-right",
        "  element-3 3>5  ←  nodes=3, 6, 5",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d roundabout-right",
        "  element-4 5>9  →  nodes=5, 7, 9",
        "    way-13  p ■   n ■   loop     fp     bp     head     tail     d roundabout-right",
        "  element-5 5>9  ←  nodes=5, 8, 9",
        "    way-13  p ■   n ■   loop     fp     bp     head     tail     d roundabout-right",
        "  element-6 9>11  ↔  nodes=9, 10, 11",
        "    way-14  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>11 nodes=1, 2, 3, 4, 5, 7, 9, 10, 11",
        "backward=11>1 nodes=11, 10, 9, 8, 5, 6, 3, 2, 1",
      )
    )
  }
}
