package kpn.server.analyzer.engine.analysis.route.structure

import kpn.core.util.UnitTest

// simple route
class Structure_N03_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(6, "02")
    memberNode(1)
    memberWay(10, "", 1, 2, 3)
    memberWay(11, "", 3, 4, 5)
    memberWay(12, "", 5, 6)
    memberNode(6)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()
    context.facts.shouldMatchTo(Set.empty)
    context.links.shouldMatchTo(
      Seq(
        "1    ",
        "2    p     n ■   loop     fp     bp     head     tail     d forward",
        "3    p ■   n ■   loop     fp     bp     head     tail     d forward",
        "4    p ■   n     loop     fp     bp     head     tail     d forward",
        "5    ",
      )
    )

    context.nodes.shouldMatchTo(
      Seq(
        "start=1(01)",
        "end=6(02)",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>6",
        "  element-1 1>6  1(01)  6(02)  ↔  nodes=1, 2, 3, 4, 5, 6",
        "    way-10  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-11  p ■   n ■   loop     fp     bp     head     tail     d forward",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>6 nodes=1, 2, 3, 4, 5, 6",
        "backward=6>1 nodes=6, 5, 4, 3, 2, 1",
      )
    )
  }
}
