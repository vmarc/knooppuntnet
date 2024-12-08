package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

// tentacle at start
class Structure_N05_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(4, "01")
    node(6, "02")

    memberWay(10, "", 1, 2, 3)
    memberWay(11, "", 3, 4)
    memberWay(12, "", 4, 5)
    memberWay(13, "", 5, 6)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    p     n ■   loop     fp     bp     head     tail     d forward",
        "2    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "4    p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=4(01)",
        "end=6(02)",
        "start-tentacle=1(01)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>6",
        "  element-1 1>4  1(01)  4(01)  ↔  nodes=1, 2, 3, 4",
        "    way-10  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-11  p ■   n ■   loop     fp     bp     head     tail     d forward",
        "  element-2 4>6  4(01)  6(02)  ↔  nodes=4, 5, 6",
        "    way-12  p ■   n ■   loop     fp     bp     head     tail     d forward",
        "    way-13  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=4>6 nodes=4, 5, 6",
        "backward=6>4 nodes=6, 5, 4",
        "start-tentacle=1>4 nodes=1, 2, 3, 4",
      )
    )
  }
}
