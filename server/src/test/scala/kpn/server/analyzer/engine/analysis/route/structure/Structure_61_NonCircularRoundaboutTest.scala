package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_61_NonCircularRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberRoundabout(12, "", 3, 4, 5)
    memberWay(13, "", 5, 7, 8)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n     loop     fp     bp     head     tail     d forward"
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>8",
        "  element-1 1>8  ↔  nodes=1, 2, 3, 4, 5, 7, 8",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d forward",
        "    way-13  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>8 nodes=1, 2, 3, 4, 5, 7, 8",
        "backward=8>1 nodes=8, 7, 5, 4, 3, 2, 1", // TODO redesign - there should be no backward path because of unidirectional roundabout section (at least for cycling)
      )
    )
    pending
  }
}
