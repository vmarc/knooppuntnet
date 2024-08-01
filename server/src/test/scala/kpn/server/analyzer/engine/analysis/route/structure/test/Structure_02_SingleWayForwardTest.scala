package kpn.server.analyzer.engine.analysis.route.structure

import kpn.api.custom.Fact.RouteBroken
import kpn.api.custom.Fact.RouteNotBackward
import kpn.api.custom.Fact.RouteNotContinious
import kpn.core.util.UnitTest
import kpn.server.analyzer.engine.analysis.route.structure.test.StructureTestSetupBuilder

class Structure_02_SingleWayForwardTest extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    memberWay(11, "forward", 1, 2, 3)
  }.build

  test("analyze") {
    val context = setup.analyze()
    context.facts.shouldMatchTo(Set(RouteNotBackward, RouteNotContinious, RouteBroken))
    context.links.shouldMatchTo(
      Seq(
        "1    p     n     loop     fp ■   bp     head ■   tail     d forward",
      )
    )

    context.segments.shouldMatchTo(
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  →  nodes=1, 2, 3",
        "    way-11  p     n     loop     fp ■   bp     head ■   tail     d forward",
      )
    )

    context.paths.shouldMatchTo(
      Seq(
        "forward=1>3 nodes=1, 2, 3",
      )
    )
  }
}
