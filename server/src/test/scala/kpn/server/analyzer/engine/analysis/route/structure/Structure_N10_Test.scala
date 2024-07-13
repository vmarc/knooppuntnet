package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// split way with forward and forward roles
class Structure_N10_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(10, "02")

    memberWay(101, "", 1, 2)
    memberWay(102, "", 2, 3)
    memberWay(103, "forward", 3, 4)
    memberWay(104, "forward", 4, 5)
    memberWay(105, "forward", 5, 6)
    memberWay(106, "forward", 7, 3)
    memberWay(107, "forward", 8, 7)
    memberWay(108, "forward", 6, 8)
    memberWay(109, "", 6, 9)
    memberWay(110, "", 9, 10)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "4    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "5    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "6    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "7    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "8    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "9    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "10    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=1(01)",
        "end=10(02)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>10",
        "  element-1 bidirectional 1>3  1(01)",
        "    way-101  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "    way-102  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "  element-2 forward 3>6",
        "    way-103  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward  paths=2",
        "    way-104  p ■   n ■   loop     fp ■   bp     head     tail     d forward  paths=2",
        "    way-105  p ■   n ■   loop     fp ■   bp     head     tail     d forward  paths=2",
        "  element-3 backward 3>6",
        "    way-106  p ■   n ■   loop     fp     bp ■   head     tail     d backward  paths=3",
        "    way-107  p ■   n ■   loop     fp     bp ■   head     tail     d backward  paths=3",
        "    way-108  p ■   n ■   loop     fp     bp ■   head     tail ■   d backward  paths=3",
        "  element-4 bidirectional 6>10  10(02)",
        "    way-109  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=4",
        "    way-110  p ■   n     loop     fp     bp     head     tail     d forward  paths=4",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1 ↔ elements=1, nodes=1, 2, 3",
        "path-2 → elements=2, nodes=3, 4, 5, 6",
        "path-3 ← elements=3, nodes=6, 8, 7, 3",
        "path-4 ↔ elements=4, nodes=6, 9, 10",
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>10 nodes=1, 2, 3, 4, 5, 6, 9, 10",
        "backward=10>1 nodes=10, 9, 6, 8, 7, 3, 2, 1",
      )
    )
  }
}
