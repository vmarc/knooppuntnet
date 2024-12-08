package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

// reproduces situation in route 16786092 (EV1 Morlaix — Carhaix-Plouguer)
class Structure_75_Split_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "forward", 1, 2)
    memberWay(12, "forward", 2, 3)
    memberWay(13, "forward", 3, 4)
    memberWay(14, "backward", 1, 5)
    memberWay(15, "backward", 5, 6)
    memberWay(16, "backward", 6, 4)
    memberWay(17, "", 4, 7)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp ■   bp     head ■   tail     d forward",
        "2    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "5    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "6    p ■   n ■   loop     fp     bp ■   head     tail ■   d forward",
        "7    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>7",
        "  element-1 1>4  →  nodes=1, 2, 3, 4",
        "    way-11  p     n ■   loop     fp ■   bp     head ■   tail     d forward",
        "    way-12  p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "    way-13  p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "  element-2 1>4  ←  nodes=1, 5, 6, 4",
        "    way-14  p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "    way-15  p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "    way-16  p ■   n ■   loop     fp     bp ■   head     tail ■   d forward",
        "  element-3 4>7  ↔  nodes=4, 7",
        "    way-17  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>7 nodes=1, 2, 3, 4, 7",
        "backward=7>1 nodes=7, 4, 6, 5, 1",
      )
    )
  }
}
