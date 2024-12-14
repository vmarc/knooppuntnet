package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteBroken
import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotContinious
import kpn.core.util.UnitTest

class Structure_03_SingleWayBackwardTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "backward", 1, 2, 3)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set(RouteNotBackward, RouteNotContinious, RouteBroken))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 3>1",
        "  element-1 3>1  →  nodes=3, 2, 1",
        "    way-11  p     n     loop     fp ■   bp     head ■   tail     d backward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=3>1 nodes=3, 2, 1",
      )
    )
  }
}
