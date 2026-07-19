package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest

class Structure_01_SingleWayBidirectionalTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
  }.build

  test("analyze") {
    val context = setup.analyze()
    assertEqual(context.facts, Set.empty)
    assertEqual(
      context.links,
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  ↔  nodes=1, 2, 3",
        "    way-11  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>3 nodes=1, 2, 3",
        "backward=3>1 nodes=3, 2, 1",
      )
    )
  }
}
