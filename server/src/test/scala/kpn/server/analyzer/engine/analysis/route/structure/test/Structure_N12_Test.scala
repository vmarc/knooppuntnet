package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotForward
import kpn.core.util.UnitTest

// broken in simple route
class Structure_N12_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(6, "02")
    memberNode(1)
    memberWay(10, "", 1, 2, 3) // broken
    memberWay(11, "", 4, 5)
    memberWay(12, "", 5, 6)
    memberNode(6)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()

    assertEqual(
      context.facts,
      Set(RouteNotForward, RouteNotBackward)
    )

    assertEqual(
      context.links,
      Seq(
        "1    ",
        "2    p     n     loop     fp     bp     head     tail     d unconnected",
        "3    p     n ■   loop     fp     bp     head     tail     d forward",
        "4    p ■   n     loop     fp     bp     head     tail     d forward",
        "5    ",
      )
    )

    assertEqual(
      context.nodes,
      Seq(
        "start=1(01)",
        "end=6(02)",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  1(01)  ↔  nodes=1, 2, 3",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected",
        "segment-2 4>6",
        "  element-2 4>6  6(02)  ↔  nodes=4, 5, 6",
        "    way-11  p     n ■   loop     fp     bp     head     tail     d forward",
        "    way-12  p ■   n     loop     fp     bp     head     tail     d forward",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "other=1>3 nodes=1, 2, 3",
        "other=4>6 nodes=4, 5, 6",
      )
    )
  }
}
