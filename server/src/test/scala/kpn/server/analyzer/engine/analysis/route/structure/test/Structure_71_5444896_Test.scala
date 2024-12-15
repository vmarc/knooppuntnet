package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest

// reproduces situation in route 5444896 (EV1 Roscoff — Morlaix)
class Structure_71_5444896_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 2, 1) // 1, 2
    // down:
    memberWay(12, "backward", 3, 2) // 2, 3
    memberWay(13, "forward", 3, 4) // 3, 4
    memberWay(14, "backward", 5, 4) // 4, 5
    memberWay(15, "backward", 8, 5) // 5, 8
    // up:
    memberWay(16, "forward", 6, 2) // 6, 2
    memberWay(17, "forward", 7, 6) // 7, 6
    memberWay(18, "backward", 7, 8) // 8, 7
    // continue:
    memberWay(19, "", 9, 8)
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
        "1    p     n ■   loop     fp     bp     head     tail     d backward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d backward",
        "3    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp ■   bp     head     tail     d backward",
        "5    p ■   n ■   loop     fp ■   bp     head     tail     d backward",
        "6    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "7    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "8    p ■   n ■   loop     fp     bp ■   head     tail ■   d forward",
        "9    p ■   n     loop     fp     bp     head     tail     d backward"
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>9",
        "  element-1 1>2  ↔  nodes=1, 2",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d backward",
        "  element-2 2>8  →  nodes=2, 3, 4, 5, 8",
        "    way-12  p ■   n ■   loop     fp ■   bp     head ■   tail     d backward",
        "    way-13  p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "    way-14  p ■   n ■   loop     fp ■   bp     head     tail     d backward",
        "    way-15  p ■   n ■   loop     fp ■   bp     head     tail     d backward",
        "  element-3 2>8  ←  nodes=2, 6, 7, 8",
        "    way-16  p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "    way-17  p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "    way-18  p ■   n ■   loop     fp     bp ■   head     tail ■   d forward",
        "  element-4 8>9  ↔  nodes=8, 9",
        "    way-19  p ■   n     loop     fp     bp     head     tail     d backward",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>9 nodes=1, 2, 3, 4, 5, 8, 9",
        "backward=9>1 nodes=9, 8, 7, 6, 2, 1",
      )
    )
  }
}
