package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.api.custom.Fact.RouteNotForward
import kpn.core.util.UnitTest

// broken in split way backward segment
class Structure_N15_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(10, "02")
    memberWay(101, "", 1, 2)
    memberWay(102, "", 2, 3)
    memberWay(103, "forward", 3, 4)
    memberWay(104, "forward", 4, 5)
    memberWay(105, "forward", 5, 6)
    memberWay(106, "backward", 3, 7)
    memberWay(107, "backward", 7, 11) // broken
    memberWay(108, "backward", 8, 6)
    memberWay(109, "", 6, 9)
    memberWay(110, "", 9, 10)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()
    context.facts.shouldMatchTo(Set(RouteNotForward, RouteNotBackward, RouteNotContinious, RouteBroken))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "4    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "5    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "6    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "7    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "8    p ■   n     loop     fp ■   bp     head     tail     d backward",
        "9    p     n ■   loop     fp     bp     head     tail     d forward",
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
        "segment-1 1>8",
        "  element-1 bidirectional 1>3  1(01)",
        "    way-101  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "    way-102  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "  element-2 forward 3>6",
        "    way-103  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward  paths=2",
        "    way-104  p ■   n ■   loop     fp ■   bp     head     tail     d forward  paths=2",
        "    way-105  p ■   n ■   loop     fp ■   bp     head     tail     d forward  paths=2",
        "  element-3 backward 3>11",
        "    way-106  p ■   n ■   loop     fp     bp ■   head     tail     d forward  paths=3",
        "    way-107  p ■   n ■   loop     fp     bp ■   head     tail     d forward  paths=3",
        "  element-4 forward 6>8",
        "    way-108  p ■   n     loop     fp ■   bp     head     tail     d backward  paths=4",
        "segment-2 6>10",
        "  element-5 bidirectional 6>10  10(02)",
        "    way-109  p     n ■   loop     fp     bp     head     tail     d forward  paths=5",
        "    way-110  p ■   n     loop     fp     bp     head     tail     d forward  paths=5",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1 ↔ elements=1, nodes=1, 2, 3",
        "path-2 → elements=2, nodes=3, 4, 5, 6",
        "path-3 ← elements=3, nodes=11, 7, 3",
        "path-4 → elements=4, nodes=6, 8",
        "path-5 ↔ elements=5, nodes=6, 9, 10",
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "other=1>3 nodes=1, 2, 3",
        "other=3>6 nodes=3, 4, 5, 6",
        "other=11>3 nodes=11, 7, 3",
        "other=6>8 nodes=6, 8",
        "other=6>10 nodes=6, 9, 10",
      )
    )
  }
}
