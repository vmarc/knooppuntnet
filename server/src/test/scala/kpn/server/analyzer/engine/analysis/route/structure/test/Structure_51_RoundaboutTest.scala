package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

class Structure_51_RoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberRoundabout(11, "", 1, 2, 3, 4, 1)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d roundaboutright",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>1",
        "  element-1 1>1  →  nodes=1, 2, 3, 4, 1",
        "    way-11  p     n     loop     fp     bp     head     tail     d roundaboutright",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>1 nodes=1, 2, 3, 4, 1",
      )
    )
  }
}
