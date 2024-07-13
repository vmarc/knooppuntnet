package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// multiple forked tentacles at end
class Structure_N08_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(5, "02")
    node(7, "02")
    node(9, "02")

    memberWay(10, "", 1, 2) // 01
    memberWay(11, "", 2, 3)
    memberWay(12, "", 3, 4)
    memberWay(13, "", 4, 5) // first 02
    memberWay(14, "forward", 5, 6)
    memberWay(15, "forward", 6, 7) // second 02
    memberWay(16, "backward", 5, 8)
    memberWay(17, "backward", 8, 9) // third 02
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "4    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "5    p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "6    p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "7    p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "8    p ■   n     loop     fp     bp ■   head     tail     d forward",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=1(01)",
        "end=5(02)",
        "end-tentacle=7(02)",
        "end-tentacle=9(02)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>9",
        "  element-1 1>5  1(01)  5(02)  ↔  nodes=1, 2, 3, 4, 5",
        "    way-10  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-11  p ■   n ■   loop     fp     bp     head     tail     d forward",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d forward",
        "    way-13  p ■   n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 5>7  5(02)  7(02)  →  nodes=5, 6, 7",
        "    way-14  p ■   n ■   loop     fp ■   bp     head ■   tail     d forward",
        "    way-15  p ■   n ■   loop     fp ■   bp     head     tail     d forward",
        "  element-3 5>9  5(02)  9(02)  ←  nodes=5, 8, 9",
        "    way-16  p ■   n ■   loop     fp     bp ■   head     tail     d forward",
        "    way-17  p ■   n     loop     fp     bp ■   head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>5 nodes=1, 2, 3, 4, 5",
        "backward=5>1 nodes=5, 4, 3, 2, 1",
        "end-tentacle=5>7 nodes=5, 6, 7",
        "end-tentacle=5>9 nodes=5, 8, 9",
      )
    )
  }
}
