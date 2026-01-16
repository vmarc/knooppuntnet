package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteBroken
import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotForward
import kpn.core.util.UnitTest

// broken after roundabout
class Structure_N17_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(10, "02")
    memberWay(10, "", 1, 2, 4)
    memberWayWithTags(11, "", roundAboutTags, 3, 4, 5, 6, 7, 8)
    memberWay(12, "", 9, 10)
  }.build("01", "02")

  test("analyze") {

    val context = setup.analyze()

    context.facts.foreach(a => println(s""""$a","""))
    context.links.foreach(a => println(s""""$a","""))
    context.nodes.foreach(a => println(s""""$a","""))
    context.segments.foreach(a => println(s""""$a","""))
    context.paths.foreach(a => println(s""""$a","""))

    assertEqual(
      context.facts,
      Set(RouteNotForward, RouteNotBackward, RouteBroken)
    )

    assertEqual(
      context.links,
      Seq(
        "1    p     n     loop     fp     bp     head     tail     d unconnected",
        "2    p     n     loop     fp     bp     head     tail     d unconnected",
        "3    p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.nodes,
      Seq(
        "start=1(01)",
        "end=10(02)",
      )
    )

    assertEqual(
      context.segments,
      Seq(
        "segment-1 1>4",
        "  element-1 1>4  1(01)  ↔  nodes=1, 2, 4",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected",
        "segment-2 3>8",
        "  element-2 3>8  ↔  nodes=3, 4, 5, 6, 7, 8",
        "    way-11  p     n     loop     fp     bp     head     tail     d unconnected",
        "segment-3 9>10",
        "  element-3 9>10  10(02)  ↔  nodes=9, 10",
        "    way-12  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "other=1>4 nodes=1, 2, 4",
        "other=3>8 nodes=3, 4, 5, 6, 7, 8",
        "other=9>10 nodes=9, 10",
      )
    )
  }
}
