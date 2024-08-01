package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

class Structure_05_SingleWayRoundaboutNotALoopTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberRoundabout(11, "", 1, 2, 3, 4)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>4",
        "  element-1 1>4  ↔  nodes=1, 2, 3, 4",
        "    way-11  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>4 nodes=1, 2, 3, 4",
        "backward=4>1 nodes=4, 3, 2, 1", // TODO redesign - NOK
      )
    )
  }
}
