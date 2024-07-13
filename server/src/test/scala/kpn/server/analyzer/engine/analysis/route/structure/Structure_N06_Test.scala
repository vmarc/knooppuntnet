package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// tentacle at end
class Structure_N06_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(5, "02")
    node(7, "02")

    memberWay(10, "", 1, 2) // 01
    memberWay(11, "", 2, 3)
    memberWay(12, "", 3, 4)
    memberWay(13, "", 4, 5) // first 02
    memberWay(14, "", 5, 6)
    memberWay(15, "", 6, 7) // second 02
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
        "5    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "6    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=1(01)",
        "end=5(02)",
        "end-tentacle=7(02)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>7",
        "  element-1 bidirectional 1>5  1(01)  5(02)",
        "    way-10  p     n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "    way-11  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "    way-13  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=1",
        "  element-2 bidirectional 5>7  5(02)  7(02)",
        "    way-14  p ■   n ■   loop     fp     bp     head     tail     d forward  paths=2",
        "    way-15  p ■   n     loop     fp     bp     head     tail     d forward  paths=2",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "path-1 ↔ elements=1, nodes=1, 2, 3, 4, 5",
        "path-2 ↔ elements=2, nodes=5, 6, 7",
      )
    )

    context.pathDetails.shouldMatchTo(
      Seq(
        "forward=1>5 nodes=1, 2, 3, 4, 5",
        "backward=5>1 nodes=5, 4, 3, 2, 1",
        "end-tentacle=5>7 nodes=5, 6, 7",
      )
    )
  }
}
