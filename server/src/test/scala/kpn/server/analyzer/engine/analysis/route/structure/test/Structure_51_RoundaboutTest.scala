package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest

class Structure_51_RoundaboutTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberRoundabout(11, "", 1, 2, 3, 4, 1)
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
        "1    p     n     loop     fp     bp     head     tail     d roundabout-right",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>1",
        "  element-1 1>1  →  nodes=1, 2, 3, 4, 1",
        "    way-11  p     n     loop     fp     bp     head     tail     d roundabout-right",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>1 nodes=1, 2, 3, 4, 1",
      )
    )
  }
}
