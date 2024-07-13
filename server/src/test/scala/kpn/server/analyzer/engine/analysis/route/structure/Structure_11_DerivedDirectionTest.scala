package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_11_DerivedDirectionTest extends UnitTest {

  // direction of first way derived from second way
  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "", 3, 4, 5)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>5",
        "  element-1 1>5  ↔  nodes=1, 2, 3, 4, 5",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
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
