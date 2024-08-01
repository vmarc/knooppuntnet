package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

// reproduces situation in route 5444896 (EV1 Roscoff — Morlaix)
class Structure_72_SplitRoundabout_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    // before split:
    memberWay(11, "", 1, 2)
    // down until roundabout:
    memberWay(12, "forward", 2, 5)
    // up from roundabout:
    memberWay(13, "forward", 8, 2)
    memberWay(14, "forward", 4, 8)
    // roundabout:
    memberRoundabout(15, "forward", 3, 4, 5, 6, 7, 3)
    // down until roundabout:
    memberWay(16, "forward", 6, 9)
    memberWay(17, "forward", 9, 10)
    // up from roundabout
    memberWay(18, "forward", 11, 7)
    memberWay(19, "forward", 10, 11)
    // continue:
    memberWay(20, "", 10, 12)
  }.build

  test("analyze") {
    val context = setup.analyze()
    // context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "3    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "5    p ■   n ■   loop     fp ■   bp     head     tail ■   d roundaboutright",
        "6    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "7    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "8    p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "9    p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "10    p ■   n     loop     fp     bp     head     tail     d forward"
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>12",
        "  element-1 1>2  ↔  nodes=1, 2",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 2>5  →  nodes=2, 5",
        "    way-12  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "  element-3 2>4  ←  nodes=2, 8, 4",
        "    way-13  p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "    way-14  p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "  element-4 5>6  →  nodes=5, 6",
        "    way-15  p ■   n ■   loop     fp ■   bp     head     tail ■   d roundaboutright",
        "  element-5 4>7  ←  nodes=4, 3, 7",
        "    way-15  p ■   n ■   loop     fp ■   bp     head     tail ■   d roundaboutright",
        "  element-6 6>10  →  nodes=6, 9, 10",
        "    way-16  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "    way-17  p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "  element-7 7>10  ←  nodes=7, 11, 10",
        "    way-18  p ■   n ■   loop     fp     bp ■   head     tail     d backward",
        "    way-19  p ■   n ■   loop     fp     bp ■   head     tail ■   d backward",
        "  element-8 10>12  ↔  nodes=10, 12",
        "    way-20  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>12 nodes=1, 2, 5, 6, 9, 10, 12",
        "backward=12>1 nodes=12, 10, 11, 7, 3, 4, 8, 2, 1",
      )
    )
  }
}
