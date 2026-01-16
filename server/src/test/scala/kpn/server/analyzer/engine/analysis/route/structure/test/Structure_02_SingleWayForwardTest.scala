package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteBroken
import kpn.api.common.Fact.RouteNotBackward
import kpn.core.util.UnitTest

class Structure_02_SingleWayForwardTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "forward", 1, 2, 3)
  }.build

  test("analyze") {
    val context = setup.analyze()

    assertEqual(
      context.facts,
      Set(RouteNotBackward, RouteBroken)
    )

    assertEqual(
      context.links,
      Seq(
        "1    p     n     loop     fp ■   bp     head ■   tail     d forward",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  →  nodes=1, 2, 3",
        "    way-11  p     n     loop     fp ■   bp     head ■   tail     d forward",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "forward=1>3 nodes=1, 2, 3",
      )
    )
  }
}
