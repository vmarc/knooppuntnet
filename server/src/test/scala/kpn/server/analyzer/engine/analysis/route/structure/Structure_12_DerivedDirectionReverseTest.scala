package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_12_DerivedDirectionReverseTest extends UnitTest {

  // direction of first way derived from second way
  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 3, 2, 1)
    memberWay(12, "", 3, 4, 5)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d backward",
        "2    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>5",
        "  element-1 1>5  ↔  nodes=1, 2, 3, 4, 5",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d backward",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>5 nodes=1, 2, 3, 4, 5",
        "backward=5>1 nodes=5, 4, 3, 2, 1",
      )
    )
  }
}
