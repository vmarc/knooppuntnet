package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

class Structure_21_ContinuousTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "", 3, 4, 5)
    memberWay(13, "", 5, 6, 7)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>7",
        "  element-1 1>7  ↔  nodes=1, 2, 3, 4, 5, 6, 7",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d forward",
        "    way-13  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>7 nodes=1, 2, 3, 4, 5, 6, 7",
        "backward=7>1 nodes=7, 6, 5, 4, 3, 2, 1",
      )
    )
  }
}
