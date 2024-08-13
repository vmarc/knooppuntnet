package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

class Structure_53_WayRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberRoundabout(12, "", 3, 4, 5, 6, 3)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d roundaboutright"
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  ↔  nodes=1, 2, 3",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 3>3  →  nodes=3, 4, 5, 6, 3",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d roundaboutright"
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>3 nodes=1, 2, 3, 4, 5, 6, 3",
        "backward=3>1 nodes=3, 2, 1",
      )
    )
  }
}
