package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

class Structure_45_ForwardForwardTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "", 1, 2)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "forward", 3, 5)
    memberWay(14, "forward", 4, 2)
    memberWay(15, "forward", 5, 4)
    memberWay(16, "", 5, 6)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "5    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "6    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>6",
        "  element-1 1>2  ↔  nodes=1, 2",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 2>5  →  nodes=2, 3, 5",
        "    way-12  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "    way-13  p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "  element-3 2>5  ←  nodes=2, 4, 5",
        "    way-14  p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "    way-15  p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "  element-4 5>6  ↔  nodes=5, 6",
        "    way-16  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>6 nodes=1, 2, 3, 5, 6",
        "backward=6>1 nodes=6, 5, 4, 2, 1",
      )
    )
  }
}
