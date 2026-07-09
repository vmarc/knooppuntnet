package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest

class Structure_22_ContinuousFirstWayReversedTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 3, 2, 1)
    memberWay(12, "", 3, 4, 5)
    memberWay(13, "", 5, 6, 7)
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
        "1    p     n ■   loop     fp     bp     head     tail     d backward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>7",
        "  element-1 1>7  ↔  nodes=1, 2, 3, 4, 5, 6, 7",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d backward",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d forward",
        "    way-13  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>7 nodes=1, 2, 3, 4, 5, 6, 7",
        "backward=7>1 nodes=7, 6, 5, 4, 3, 2, 1",
      )
    )
  }
}
