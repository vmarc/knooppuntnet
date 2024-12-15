package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest

class Structure_41_ForwardNoBackwardPathTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "forward", 1, 2)
    memberWay(12, "", 3, 2)
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
        "1    p     n ■   loop     fp ■   bp     head ■   tail     d forward",
        "2    p ■   n     loop     fp     bp     head     tail     d backward",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>3",
        "  element-1 1>2  →  nodes=1, 2",
        "    way-11  p     n ■   loop     fp ■   bp     head ■   tail     d forward",
        "  element-2 2>3  ↔  nodes=2, 3",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d backward",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>3 nodes=1, 2, 3",
        "backward=3>2 nodes=3, 2",
      )
    )
  }
}
