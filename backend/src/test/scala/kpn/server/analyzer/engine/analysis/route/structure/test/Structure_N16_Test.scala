package kpn.server.analyzer.engine.analysis.route.structure.test

import kpn.api.common.Fact.RouteNotBackward
import kpn.api.common.Fact.RouteNotForward
import kpn.core.util.UnitTest

// broken at roundabout
class Structure_N16_Test extends UnitTest {

  private def setup = new StructureTestSetupBuilder() {
    node(1, "01")
    node(10, "02")
    memberWay(10, "", 1, 2)
    memberWayWithTags(11, "", roundAboutTags, 3, 4, 5, 6, 7, 8)
    memberWay(12, "", 7, 9, 10)
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
      Set(RouteNotForward, RouteNotBackward)
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
        "segment-1 1>2",
        "  element-1 1>2  1(01)  ↔  nodes=1, 2",
        "    way-10  p     n     loop     fp     bp     head     tail     d unconnected",
        "segment-2 3>8",
        "  element-2 3>8  ↔  nodes=3, 4, 5, 6, 7, 8",
        "    way-11  p     n     loop     fp     bp     head     tail     d unconnected",
        "segment-3 7>10",
        "  element-3 7>10  10(02)  ↔  nodes=7, 9, 10",
        "    way-12  p     n     loop     fp     bp     head     tail     d unconnected",
      )
    )

    assertEqual(
      context.paths,
      Seq(
        "other=1>2 nodes=1, 2",
        "other=3>8 nodes=3, 4, 5, 6, 7, 8",
        "other=7>10 nodes=7, 9, 10",
      )
    )
  }
}
