package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

class Structure_52_RoundaboutRoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberRoundabout(11, "", 1, 2, 3, 4, 1)
    memberRoundabout(12, "", 3, 5, 6, 7, 3)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop ■   fp     bp     head     tail     d roundaboutright",
        "2    p ■   n     loop ■   fp     bp     head     tail     d roundaboutright"
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  →  nodes=1, 2, 3",
        "    way-11  p     n ■   loop ■   fp     bp     head     tail     d roundaboutright",
        "  element-2 1>3  ←  nodes=1, 4, 3",
        "    way-11  p     n ■   loop ■   fp     bp     head     tail     d roundaboutright",
        "  element-3 3>3  →  nodes=3, 5, 6, 7, 3",
        "    way-12  p ■   n     loop ■   fp     bp     head     tail     d roundaboutright",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>3 nodes=1, 2, 3, 5, 6, 7, 3",
        "backward=3>1 nodes=3, 4, 1",
      )
    )
  }
}
