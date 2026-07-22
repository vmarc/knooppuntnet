package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest

class Structure_62_NonCircularRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberRoundabout(12, "forward", 3, 4, 5)
    memberRoundabout(13, "forward", 5, 6, 3)
    memberWay(14, "", 5, 7, 8)
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
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "3    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "4    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>8",
        "  element-1 1>3  ↔  nodes=1, 2, 3",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 3>5  →  nodes=3, 4, 5",
        "    way-12  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "  element-3 3>5  ←  nodes=3, 6, 5",
        "    way-13  p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "  element-4 5>8  ↔  nodes=5, 7, 8",
        "    way-14  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>8 nodes=1, 2, 3, 4, 5, 7, 8",
        "backward=8>1 nodes=8, 7, 5, 6, 3, 2, 1",
      )
    )
  }
}
