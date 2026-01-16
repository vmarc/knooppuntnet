package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotForward
import kpn.core.util.UnitTest

class Structure_42_ForwardGapTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "forward", 2, 1)
    memberWay(12, "", 3, 2)
  }.build

  test("analyze") {
    val context = setup.analyze()

    assertEqual(
      context.facts,
      Set(RouteNotForward, RouteNotBackward)
    )

    assertEqual(
      context.links,
      Seq(
        "1    p     n     loop     fp ■   bp     head ■   tail     d forward",
        //
        "2    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 2>1",
        "  element-1 2>1  →  nodes=2, 1",
        "    way-11  p     n     loop     fp ■   bp     head ■   tail     d forward",
        "segment-2 3>2",
        "  element-2 3>2  ↔  nodes=3, 2",
        "    way-12  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "other=2>1 nodes=2, 1",
        "other=3>2 nodes=3, 2",
      )
    )
  }
}
