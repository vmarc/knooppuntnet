package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest

// split way with forward and backward roles
class Structure_N09_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(10, "02")

    memberWay(101, "", 1, 2)
    memberWay(102, "", 2, 3)
    memberWay(103, "forward", 3, 4)
    memberWay(104, "forward", 4, 5)
    memberWay(105, "forward", 5, 6)
    memberWay(106, "backward", 3, 7)
    memberWay(107, "backward", 7, 8)
    memberWay(108, "backward", 8, 6)
    memberWay(109, "", 6, 9)
    memberWay(110, "", 9, 10)
  }.build("01", "02")

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
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "4    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "5    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "6    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "7    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "8    p ■   n ■   loop     fp     bp ■   head     tail ■   d forward",
        "9    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "10    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    assertEqual(
      context.nodes,
      Seq(
        "start=1(01)",
        "end=10(02)",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>10",
        "  element-1 1>3  1(01)  ↔  nodes=1, 2, 3",
        "    way-101  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-102  p ■   n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 3>6  →  nodes=3, 4, 5, 6",
        "    way-103  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "    way-104  p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "    way-105  p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "  element-3 3>6  ←  nodes=3, 7, 8, 6",
        "    way-106  p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "    way-107  p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "    way-108  p ■   n ■   loop     fp     bp ■   head     tail ■   d forward",
        "  element-4 6>10  10(02)  ↔  nodes=6, 9, 10",
        "    way-109  p ■   n ■   loop     fp     bp     head     tail     d forward",
        "    way-110  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>10 nodes=1, 2, 3, 4, 5, 6, 9, 10",
        "backward=10>1 nodes=10, 9, 6, 8, 7, 3, 2, 1",
      )
    )
  }
}
