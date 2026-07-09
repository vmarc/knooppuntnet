package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest

// reproduces situation in route 16786092 (EV1 Morlaix — Carhaix-Plouguer)
class Structure_73_Split_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "forward", 1, 2)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "backward", 1, 4)
    memberWay(14, "backward", 4, 3)
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
        "2    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "4    p ■   n     loop     fp     bp ■   head     tail ■   d forward",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  →  nodes=1, 2, 3",
        "    way-11  p     n ■   loop     fp ■   bp     head ■   tail     d forward",
        "    way-12  p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "  element-2 1>3  ←  nodes=1, 4, 3",
        "    way-13  p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "    way-14  p ■   n     loop     fp     bp ■   head     tail ■   d forward",
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
