package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest

// reproduces situation in route 16842517 (EV1 Mimizan Plage — Léon)
class Structure_76_Split_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "backward", 2, 1)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "forward", 4, 1)
    memberWay(14, "forward", 3, 4)
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
        "1    p     n ■   loop     fp ■   bp     head ■   tail     d backward",
        "2    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "4    p ■   n     loop     fp     bp ■   head     tail ■   d backward",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  →  nodes=1, 2, 3",
        "    way-11  p     n ■   loop     fp ■   bp     head ■   tail     d backward",
        "    way-12  p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "  element-2 1>3  ←  nodes=1, 4, 3",
        "    way-13  p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "    way-14  p ■   n     loop     fp     bp ■   head     tail ■   d backward",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>3 nodes=1, 2, 3",
        "backward=3>1 nodes=3, 4, 1",
      )
    )
  }
}
