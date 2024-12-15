package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest

class Structure_14_DerivedDirectionBackwardTest extends UnitTest {

  // direction of first way derived from second way - second way backward
  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2, 3)
    memberWay(12, "backward", 5, 4, 3)
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
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>5",
        "  element-1 1>3  ↔  nodes=1, 2, 3",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 3>5  →  nodes=3, 4, 5",
        "    way-12  p ■   n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>5 nodes=1, 2, 3, 4, 5",
        "backward=3>1 nodes=3, 2, 1",
      )
    )
  }
}
