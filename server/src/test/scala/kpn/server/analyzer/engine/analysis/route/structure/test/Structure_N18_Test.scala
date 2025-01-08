package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteOneWay
import kpn.api.custom.ScopedRouteType
import kpn.api.custom.Tags
import kpn.core.util.UnitTest

// oneway route -> oneway=yes
class Structure_N18_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(3, "02")
    memberWayWithTags(10, "", Tags.from("highway" -> "road", "oneway" -> "yes"), 1, 2, 3)
  }.build("01", "02", ScopedRouteType.rwn, Tags.from("oneway" -> "yes"))

  test("analyze") {

    val context = setup.analyze()

    assertEqual(
      context.facts,
      Set(RouteOneWay)
    )

    assertEqual(
      context.links,
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.nodes,
      Seq(
        "start=1(01)",
        "end=3(02)",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>3",
        "  element-1 1>3  1(01)  3(02)  ↔  nodes=1, 2, 3",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected",
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
